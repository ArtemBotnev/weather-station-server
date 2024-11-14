package com.artembotnev.weather.station

import com.artembotnev.weather.station.domain.DateTimeService
import kotlin.test.Test

class DateTimeServiceTest {

    @Test
    fun getDateTimeTest() {
        val dateTime = DateTimeService().getDateTimePack(3)
        println(dateTime)
    }
}