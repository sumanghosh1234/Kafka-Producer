package com.oyster.dto

import java.math.BigDecimal
import java.util.*

data class Customer (var balance : BigDecimal = BigDecimal.valueOf(0), val transactions : Deque<Transaction> = LinkedList()) {

    fun addLastTransaction(transaction: Transaction) {
        transactions.add(transaction)
    }

    fun popLastTransaction() : Transaction {
        return transactions.pollLast()
    }

    fun chargeFare(fare :BigDecimal) {
        this.balance = this.balance.subtract(fare)
    }

    fun chargeBackFare(fare :BigDecimal) {
        this.balance = this.balance.add(fare)
    }
}