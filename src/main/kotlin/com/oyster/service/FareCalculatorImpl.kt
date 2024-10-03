package com.oyster.service

import com.oyster.dto.Journey
import com.oyster.rule.*
import java.math.BigDecimal

class FareCalculatorImpl : FareCalculator {

    private val maximumFare :BigDecimal = BigDecimal.valueOf(3.20)
    private val fareRules : List<FareRule> = listOf(BusFareRule(), SameZoneInsideZoneOneFareRule(), SameZoneOutsideZoneOneFareRule(), TwoZonesIncludingOneFareRule(), TwoZonesExcludingOneFareRule())


    override fun calculateJourney(journey: Journey): BigDecimal {
        var fare : BigDecimal = maximumFare

        if(journey.to != null) {
            for(toZone in journey.to.zones) {
                for(fromZone in journey.from.zones) {
                    for (rule in fareRules) {
                        if(rule.shouldProcess(journey.type, fromZone, toZone)) {
                            val ruleFare = rule.getFare()
                            fare = fare.min(ruleFare)
                        }
                    }
                }
            }
        }


        return fare
    }

}
