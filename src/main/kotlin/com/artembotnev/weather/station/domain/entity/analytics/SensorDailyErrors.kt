package com.artembotnev.weather.station.domain.entity.analytics

import kotlinx.serialization.Serializable

@Serializable
internal data class SensorDailyErrors(
    val sensorId: String,
    val sensorName: String?,
    val totalMeasures: Int,
    val errorCount: Int,
    val errorPercent: Double,
)