package com.artembotnev.weather.station.domain.service

import com.artembotnev.weather.station.domain.data.MeasureRepository

internal class DeviceService(private val repository: MeasureRepository) {
    suspend fun getDevices() = repository.getDevices()
}