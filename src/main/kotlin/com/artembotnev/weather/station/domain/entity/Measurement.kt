package com.artembotnev.weather.station.domain.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Measurement(
    val timestamp: String? = null,
    val timeZoneHours: Int,
    val device: Device,
    val measures: List<Measure>,
    @Transient val currentDay: Int = 0,
    @Transient val isNewDay: Boolean = false,
)