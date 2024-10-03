package com.oyster.service

import com.oyster.dto.Customer
import com.oyster.dto.JourneyType
import com.oyster.dto.Station

interface CustomerJourney {

    fun inwardTap(customer : Customer, station: Station, journeyType: JourneyType)

    fun outwardTap(customer: Customer, station: Station)
}
