package com.artembotnev.weather.station.domain

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.domain.entity.Measurement


internal class MeasureService(private val repository: MeasureRepository) {

    suspend fun setMeasurement(measurement: Measurement?) {
        repository.setMeasurement(measurement)
    }

    suspend fun getMeasurement(deviceId: Int) = repository.getMeasurement(deviceId)
}