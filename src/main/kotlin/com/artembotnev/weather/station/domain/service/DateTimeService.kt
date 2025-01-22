package com.artembotnev.weather.station.domain.service

import com.artembotnev.weather.station.domain.entity.DateTimePack
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration

internal class DateTimeService {

    /*
    * @param timeZone - time zone
    * @param dateTime - date time
    *
    * @return DateTimePack for current date time if param dateTime is null else DateTimePack for param dateTime
    *  */
    fun getDateTimePack(timeZone: Int, dateTime: DateTime? = null): DateTimePack {
        val resultDateTime = dateTime?.withZone(timeZone) ?: getDataTimeWithTimeZone(timeZone)

        return DateTimePack(
            dayOfMonth = resultDateTime.dayOfMonth,
            timestamp = resultDateTime.toString(),
        )
    }

    fun getCurrentDateTime(): DateTime = DateTime()

    fun getDurationStringInHours(startTime: DateTime?, endTime: DateTime?): String? {
        if (startTime == null || endTime == null) return null

        val durationMinutes = Duration(startTime, endTime).standardMinutes
        val minutes = durationMinutes % 60
        val hours = durationMinutes / 60

        return "$hours:$minutes"
    }

    private fun DateTime.withZone(timeZone: Int): DateTime = this
        .withZone((DateTimeZone.forID("GMT")))
        .withZone(DateTimeZone.forOffsetHours(timeZone))

    private fun getDataTimeWithTimeZone(timeZone: Int) = DateTime()
        .withZone((DateTimeZone.forID("GMT")))
        .withZone(DateTimeZone.forOffsetHours(timeZone))
}