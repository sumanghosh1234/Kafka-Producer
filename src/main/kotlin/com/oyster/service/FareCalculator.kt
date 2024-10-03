package com.oyster.service

import com.oyster.dto.Journey
import java.math.BigDecimal

interface FareCalculator {
    fun calculateJourney(journey: Journey): BigDecimal
}