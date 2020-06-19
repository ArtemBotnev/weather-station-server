package com.artembotnev.weatherstationserver.data

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Im memory cache
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class Cache {
    // Map with device id as a key and device as a value
    val deviceMap: MutableMap<Int, Device> = HashMap()
    // Map with device id as a key and measurement as a value
    val lastMeasurements: MutableMap<Int, Measurement> = HashMap()
    // Map with device id as a key and device token as a value
    var tokenMap: Map<Int, String>? = null
}