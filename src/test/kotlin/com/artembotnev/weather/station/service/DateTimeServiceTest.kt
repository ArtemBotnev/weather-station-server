package com.artembotnev.weather.station.service

import com.artembotnev.weather.station.domain.service.DateTimeService
import org.joda.time.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeServiceTest {

    @Test
    fun getDateTimeTest() {
        val dateTime = DateTimeService().getDateTimePack(3)
        println(dateTime)
    }

    @Test
    fun getDurationStringInHoursTest() {
        val startDate = DateTime()
        val endDate = startDate
            .plusHours(34)
            .plusMinutes(17)

        assertEquals("34:17", DateTimeService().getDurationStringInHours(startDate, endDate))
    }
}