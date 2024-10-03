package com.oyster.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import com.oyster.dto.Journey
import com.oyster.dto.JourneyType
import com.oyster.dto.Station
import com.oyster.dto.Zone
import java.math.BigDecimal

class FareCalculatorTests {

    private lateinit var fareCalculator : FareCalculator

    @BeforeEach
    fun setup() {
        fareCalculator = FareCalculatorImpl()
    }

    @DisplayName("Any bus journey £1.80")
    @Test
    internal fun busJourneysMustBeOnePoundEighty() {
        val busJourney = Journey(generateStation(),generateStation(), JourneyType.BUS)

        val expected : BigDecimal = BigDecimal.valueOf(1.80)

        val result : BigDecimal = fareCalculator.calculateJourney(busJourney)

        Assertions.assertEquals(expected, result)
    }

    @DisplayName("Anywhere in Zone 1 £2.50")
    @Test
    internal fun tubeJourneyInZoneOneMustBeTwoPoundsFifty() {
        val journey = Journey(
            Station("DefaultStation",setOf(Zone.ONE)),
            Station("DefaultStation",setOf(Zone.ONE)),
            JourneyType.TUBE)

        val expected = BigDecimal.valueOf(2.50)

        val result = fareCalculator.calculateJourney(journey)

        Assertions.assertEquals(expected, result)
    }

    @DisplayName("Any one zone outside zone 1 £2.00")
    @Test
    internal fun tubeJourneyInOneZoneOutsideOfOneMustBeTwoPounds() {
        val journey = Journey(
            Station("DefaultStation",setOf(Zone.TWO)),
            Station("DefaultStation",setOf(Zone.TWO)),
            JourneyType.TUBE)

        val expected = BigDecimal.valueOf(2.00)

        val result = fareCalculator.calculateJourney(journey)

        Assertions.assertEquals(expected, result)
    }

    @DisplayName("Any two zones including zone 1 £3.00")
    @Test
    internal fun tubeJourneyInTwoZonesIncludingZoneOneMustBeThreePounds() {
        val journey = Journey(
            Station("DefaultStation",setOf(Zone.ONE)),
            Station("DefaultStation",setOf(Zone.TWO)),
            JourneyType.TUBE)

        val expected = BigDecimal.valueOf(3.00)

        val result = fareCalculator.calculateJourney(journey)

        Assertions.assertEquals(expected, result)
    }

    @DisplayName("Any two zones excluding zone 1 £2.25")
    @Test
    internal fun tubeJourneyInTwoZonesExcludingZoneOneMustBeTwoPoundsTwentyFive() {
        val journey = Journey(
            Station("DefaultStation", setOf(Zone.TWO)), Station("DefaultStation", setOf(Zone.THREE)),
            JourneyType.TUBE)

        val expected = BigDecimal.valueOf(2.25)

        val result = fareCalculator.calculateJourney(journey)

        Assertions.assertEquals(expected, result)
    }

    @DisplayName("Any three zones £3.20")
    @Test
    internal fun tubeJourneyWithoutAToStationMustBeThreePoundsTwenty() {
        val journey = Journey(from = Station("DefaultStation", setOf(Zone.TWO)), type = JourneyType.TUBE)

        val expected = BigDecimal.valueOf(3.20)

        val result = fareCalculator.calculateJourney(journey)

        Assertions.assertEquals(expected, result)
    }

    @DisplayName("The system should favour the customer where more than one fare is possible for a given " +
            "journey.")
    @Test
    internal fun tubeJourneyWithStationsWithMultipleZonesMustFavourLowestFare() {
        val journey = Journey(
            Station("DefaultStation", setOf(Zone.ONE)), Station("DefaultStation", setOf(Zone.ONE, Zone.TWO)),
            JourneyType.TUBE)

        val expected = BigDecimal.valueOf(2.50)

        val result = fareCalculator.calculateJourney(journey)

        Assertions.assertEquals(expected, result)
    }

    @DisplayName("When the user passes through the inward barrier at the station, their oyster card is charged " +
            "the maximum fare.")
    @Test
    internal fun mustChargeMaximumFareForOnlyInwardUse() {
        val journey = Journey(from = Station("defaultStation", setOf()), type = JourneyType.TUBE)
        val expected = BigDecimal.valueOf(3.20)
        val result = fareCalculator.calculateJourney(journey)
        Assertions.assertEquals(expected, result)
    }

    private fun generateStation(): Station {
        return Station("DefaultStation", setOf(Zone.ONE))
    }
}