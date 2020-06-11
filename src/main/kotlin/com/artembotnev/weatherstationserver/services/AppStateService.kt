package com.artembotnev.weatherstationserver.services

import com.artembotnev.weatherstationserver.AppState
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Created by Artem Botnev on June 10 2020
 */
@Service
class AppStateService {

    @Value("\${spring.application.name}")
    lateinit var appName: String

    fun getCurrentStateMessage() = AppState().toString()
}