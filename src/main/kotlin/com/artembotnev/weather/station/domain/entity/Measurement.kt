package com.artembotnev.weather.station.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Measurement(
    val timestamp: String?,
    val timeZoneHours: Int,
    val device: Device,
    val measures: List<Measure>
)