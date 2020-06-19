package com.artembotnev.weatherstationserver.services

import com.artembotnev.weatherstationserver.AppState
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AppStateService {
    @Value("\${spring.application.name}")
    private lateinit var appName: String

    fun getCurrentStateMessage() = AppState(appName).toString()
}