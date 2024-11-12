package com.artembotnev.weather.station.domain.data

import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement
import kotlinx.coroutines.flow.SharedFlow


internal interface MeasureRepository {
    suspend fun setMeasurement(measurement: Measurement?)
    suspend fun getMeasurement(deviceId: Int): Measurement?
    suspend fun getMeasureDailyCalculation(sensorId: String): MeasureDailyCalculation?
    suspend fun save
    fun observeMeasure(): SharedFlow<Measurement>
}