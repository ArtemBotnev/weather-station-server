package com.artembotnev.weather.station.storage

import com.artembotnev.weather.station.domain.entity.Measurement
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

internal class MeasureInMemoryStorageImpl : MeasureInMemoryStorage {

    private val cache = Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterAccess(1, TimeUnit.DAYS)
        .softValues()
        .build<Int, Measurement>()

    private val mutex = Mutex()

    override suspend fun setMeasurement(measurement: Measurement) {
        mutex.withLock {
            cache.put(measurement.device.id, measurement)
        }
    }

    override suspend fun getMeasurement(deviceId: Int): Measurement? = cache.getIfPresent(deviceId)
}