package com.oyster.service

import com.oyster.dto.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

import java.math.BigDecimal
import java.util.*

class CustomerJourneyTests {

    lateinit var fareCalculator: FareCalculator
    lateinit var customerJourney: CustomerJourney

    @BeforeEach
    fun setup() {
        fareCalculator = mock()
        customerJourney = CustomerJourneyImpl(fareCalculator)
    }

    @Test
    internal fun mustAddTransactionWhenDoingInwardTap() {
        val customer = Customer()

        val station = Station("First Station", setOf())
        val type = JourneyType.TUBE
        val expectedFare = BigDecimal.valueOf(10.0)

        whenever(fareCalculator.calculateJourney(any())).thenReturn(expectedFare)

        customerJourney.inwardTap(customer, station, type)

        Assertions.assertFalse(customer.transactions.isEmpty())
        val transaction: Transaction = customer.transactions.last
        Assertions.assertEquals(expectedFare, transaction.fare)
        val transactionJourney: Journey = transaction.journey
        Assertions.assertEquals(station, transactionJourney.from)
        Assertions.assertEquals(type, transactionJourney.type)
    }

    @Test
    internal fun mustChargeFareOnInwardsTap() {
        val customerBalance = BigDecimal.valueOf(50.0)
        val customer = Customer(balance = customerBalance)
        val calculatedFare = BigDecimal.valueOf(10.0)
        val expectedBalance = customerBalance.subtract(calculatedFare)
        whenever(fareCalculator.calculateJourney(any())).thenReturn(calculatedFare)

        customerJourney.inwardTap(customer, Station("DefaultStation", setOf()), JourneyType.TUBE)

        Assertions.assertEquals(expectedBalance, customer.balance)
    }

    @Test
    internal fun mustReplaceLastTransactionOnOutwardsTap() {
        val journeyType = JourneyType.TUBE
        val journeyStation = Station("First Station", setOf())
        val journeyDestination = Station("Second Station", setOf())
        val lastTransactionJourney = Journey(journeyStation,journeyDestination,journeyType)
        val lastTransactionFare = BigDecimal.valueOf(50.0)
        val lastTransaction = Transaction(lastTransactionFare, lastTransactionJourney)
        val customer = Customer(transactions = LinkedList(listOf(lastTransaction)))
        val newTransactionFare = BigDecimal.valueOf(20.0)

        whenever(fareCalculator.calculateJourney(any())).thenReturn(newTransactionFare)

        customerJourney.outwardTap(customer, journeyDestination)

        Assertions.assertEquals(1, customer.transactions.size)

        val resultTransaction = customer.transactions.last

        Assertions.assertEquals(newTransactionFare, resultTransaction.fare)
        val resultTransactionJourney = resultTransaction.journey

        Assertions.assertEquals(journeyStation, resultTransactionJourney.from)
        Assertions.assertEquals(journeyType, resultTransactionJourney.type)
        Assertions.assertEquals(journeyDestination, resultTransactionJourney.to)
    }

    @Test
    internal fun mustChargeBackPreviousTransactionFareThenChargeNewTransactionFareOnOutwardsTap() {

        val lastTransactionFare = BigDecimal.valueOf(20.0)
        val lastTransaction = Transaction(lastTransactionFare, Journey(Station("DefaultStation", setOf()),Station("DefaultStation", setOf()),JourneyType.TUBE))
        val customerBalance = BigDecimal.valueOf(50.0)
        val customer = Customer(customerBalance, LinkedList(listOf(lastTransaction)))
        val calculatedFare = BigDecimal.valueOf(10.0)
        val expectedBalance = customerBalance.add(lastTransactionFare).subtract(calculatedFare)
        whenever(fareCalculator.calculateJourney(any())).thenReturn(calculatedFare)

        customerJourney.outwardTap(customer, Station("DefaultStation", setOf()))

        Assertions.assertEquals(expectedBalance, customer.balance)
    }
}