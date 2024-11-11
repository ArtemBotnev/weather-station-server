package com.artembotnev.weather.station.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val id: Int,
    val type: String?,
    val name: String?,
    val location: String?,
)
