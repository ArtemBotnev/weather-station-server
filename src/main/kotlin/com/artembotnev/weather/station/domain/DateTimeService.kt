package com.artembotnev.weather.station.domain

import com.artembotnev.weather.station.domain.entity.DateTimePack
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

internal class DateTimeService {

    fun getDateTimePack(timeZone: Int): DateTimePack {
        val dateTime = getDataTimeWithTimeZone(timeZone)

        return DateTimePack(
            dayOfMonth = dateTime.dayOfMonth,
            timestamp = dateTime.toString(),
        )
    }

    private fun getDataTimeWithTimeZone(timeZone: Int) = DateTime()
        .withZone((DateTimeZone.forID("GMT")))
        .withZone(DateTimeZone.forOffsetHours(timeZone))
}