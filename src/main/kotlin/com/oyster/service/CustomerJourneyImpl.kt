package com.oyster.service

import com.oyster.dto.*


class CustomerJourneyImpl(private val fareCalculator: FareCalculator) : CustomerJourney {

    override fun inwardTap(customer: Customer, station: Station, journeyType: JourneyType) {
        val journey =  Journey(from = station, type = journeyType)
        val fare = fareCalculator.calculateJourney(journey)
        customer.addLastTransaction(Transaction(fare,journey))
        customer.chargeFare(fare)
    }

    override fun outwardTap(customer: Customer, station: Station) {
        val lastTransaction = customer.popLastTransaction()
        val completedJourney = Journey(lastTransaction.journey.from, station,lastTransaction.journey.type)
        customer.chargeBackFare(lastTransaction.fare)
        val fare = fareCalculator.calculateJourney(completedJourney)
        customer.addLastTransaction(Transaction(fare,completedJourney))
        customer.chargeFare(fare)
    }

}
