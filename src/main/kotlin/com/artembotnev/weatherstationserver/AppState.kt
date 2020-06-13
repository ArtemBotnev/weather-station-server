package com.artembotnev.weatherstationserver

import java.time.Instant
import java.time.LocalTime

class AppState(private val appName: String) {
    private val utc= Instant.now()
    private val localTime = LocalTime.now()

    override fun toString() = "$appName is running\n$utc\n$localTime"
}