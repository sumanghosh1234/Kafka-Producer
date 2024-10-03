package com.oyster

import com.oyster.dto.Customer
import com.oyster.dto.JourneyType
import com.oyster.dto.Station
import com.oyster.dto.Zone
import com.oyster.service.CustomerJourneyImpl
import com.oyster.service.FareCalculatorImpl
import java.math.BigDecimal

fun main() {
    val holbornStation = Station("Holborn", setOf(Zone.ONE))
    val earlsCourtStation = Station("Earl's Court", setOf(Zone.ONE, Zone.TWO))
    val hammersmithStation = Station("Hammersmith", setOf(Zone.THREE))
    val chelseaStation = Station("Chelsea", setOf())

    val customer = Customer(balance = BigDecimal.valueOf(30.00))

    val customerJourney = CustomerJourneyImpl(FareCalculatorImpl())

    customerJourney.inwardTap(customer,holbornStation, JourneyType.TUBE)

    customerJourney.outwardTap(customer, earlsCourtStation)

    customerJourney.inwardTap(customer, earlsCourtStation, JourneyType.BUS)

    customerJourney.outwardTap(customer, chelseaStation)

    customerJourney.inwardTap(customer, earlsCourtStation, JourneyType.TUBE)

    customerJourney.outwardTap(customer, hammersmithStation)

    println("Customer balance is: £${customer.balance}")
}