package com.artembotnev.weather.station.storage

import com.artembotnev.weather.station.domain.entity.Measurement

internal interface MeasureInMemoryStorage {
    suspend fun setMeasurement(measurement: Measurement)
    suspend fun getMeasurement(deviceId: Int): Measurement?
}