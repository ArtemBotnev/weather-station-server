package com.artembotnev.weatherstationserver

import org.springframework.beans.factory.annotation.Value
import java.time.Instant
import java.time.LocalTime

class AppState {
    private val utc= Instant.now()
    private val localTime = LocalTime.now()

    @Value("\${spring.application.name}")
    private lateinit var appName: String

    override fun toString() = "$appName is running\n$utc\n$localTime"
}