package com.artembotnev.weather.station.domain.entity.analytics

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.joda.time.DateTime

@Serializable
internal data class DeviceDailyErrors(
    val deviceId: Int,
    val deviceName: String?,
    val startAnalysisTime: String? = null,
    val lastUpdateAnalysisTime: String? = null,
    val analysisDuration: String? = null,
    val timeZoneHours: Int = 0,
    val sensorErrorList: List<SensorDailyErrors>,
    @Transient val startTime: DateTime? = null,
    @Transient val lastUpdateTime: DateTime? = null,
)