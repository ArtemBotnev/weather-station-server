package com.artembotnev.weather.station.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Measure(
    val sensorId: String,
    val sensorName: String?,
    val sensorPlace: String,
    val measureName: String,
    val measureValue: Double,
    val measureUnit: String,
    val sensorError: Boolean = false,
    val dailyCalculation: DailyCalculation? = null,
) {
    @Serializable
    data class DailyCalculation(
        val maxValue: Double,
        val minValue: Double,
        val averageValue: Double,
        val maxValueTime: String?,
        val minValueTime: String?,
    )
}