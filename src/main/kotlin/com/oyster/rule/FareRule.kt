package com.oyster.rule

import com.oyster.dto.JourneyType
import com.oyster.dto.Zone
import java.math.BigDecimal

interface FareRule {
    fun shouldProcess(type : JourneyType, one : Zone, two : Zone) : Boolean
    fun getFare() : BigDecimal
}