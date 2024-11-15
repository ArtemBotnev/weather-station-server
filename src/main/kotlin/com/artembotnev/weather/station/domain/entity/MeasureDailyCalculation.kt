package com.artembotnev.weather.station.domain.entity

internal data class MeasureDailyCalculation(
    val sensorId: String, // key
    val maxValue: Double,
    val minValue: Double,
    val averageValue: Double,
    val factor: Int = 0,
)