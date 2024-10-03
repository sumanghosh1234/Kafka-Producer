package com.oyster.rule

import com.oyster.dto.JourneyType
import com.oyster.dto.Zone
import java.math.BigDecimal

class TwoZonesIncludingOneFareRule : FareRule {
    override fun shouldProcess(type: JourneyType, one: Zone, two: Zone): Boolean {
        return JourneyType.TUBE == type && (Zone.ONE == one || Zone.ONE == two) && one != two
    }

    override fun getFare(): BigDecimal {
        return BigDecimal.valueOf(3.00)
    }
}