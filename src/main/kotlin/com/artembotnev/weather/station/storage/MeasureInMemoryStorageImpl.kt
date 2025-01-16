package com.artembotnev.weather.station.storage

import com.artembotnev.weather.station.domain.entity.Device
import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement
import com.artembotnev.weather.station.domain.entity.analytics.DeviceDailyErrors
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

private const val MEASUREMENT_CACHE_SIZE = 500L
private const val MDC_CACHE_SIZE = 8_000L
private const val DEVICE_ERRORS_CACHE_SIZE = 300L
private const val CACHE_LIVE_DAYS = 1L // 1 day

internal class MeasureInMemoryStorageImpl : MeasureInMemoryStorage {

    private val measurementCache = Caffeine.newBuilder()
        .maximumSize(MEASUREMENT_CACHE_SIZE)
        .expireAfterAccess(CACHE_LIVE_DAYS, TimeUnit.DAYS)
        .softValues()
        .build<Int, Measurement>()

    private val mdcCache = Caffeine.newBuilder()
        .maximumSize(MDC_CACHE_SIZE)
        .expireAfterAccess(CACHE_LIVE_DAYS, TimeUnit.DAYS)
        .softValues()
        .build<String, MeasureDailyCalculation>()

    private val dailyErrors = Caffeine.newBuilder()
        .maximumSize(DEVICE_ERRORS_CACHE_SIZE)
        .expireAfterAccess(CACHE_LIVE_DAYS, TimeUnit.DAYS)
        .softValues()
        .build<Int, DeviceDailyErrors>()

    private val measurementMutex = Mutex()

    private val mdcMutex = Mutex()

    private val deMutex = Mutex()

    override suspend fun setMeasurement(measurement: Measurement) {
        measurementMutex.withLock {
            measurementCache.put(measurement.device.id, measurement)
        }
    }

    override suspend fun getMeasurement(deviceId: Int): Measurement? = measurementCache.getIfPresent(deviceId)

    override suspend fun setMeasureDailyCalculations(mdcList: List<MeasureDailyCalculation>) {
        mdcMutex.withLock {
            mdcList.forEach { mdcCache.put(it.sensorId, it) }
        }
    }

    override suspend fun getMeasureDailyCalculation(sensorId: String): MeasureDailyCalculation? {
        return mdcCache.getIfPresent(sensorId)
    }

    override suspend fun getDevices(): List<Device> {
        return measurementCache.asMap().keys
            .toList()
            .mapNotNull { measurementCache.getIfPresent(it)?.device }
    }

    override suspend fun setDeviceDailyErrors(deviceDailyErrors: DeviceDailyErrors) {
        deMutex.withLock {
            dailyErrors.put(deviceDailyErrors.deviceId, deviceDailyErrors)
        }
    }

    override suspend fun getDeviceDailyErrors(deviceId: Int): DeviceDailyErrors? {
        return dailyErrors.getIfPresent(deviceId)
    }

    override suspend fun getDeviceListDailyErrors(): List<DeviceDailyErrors> {
        return dailyErrors.asMap().values.toList()
    }

    override suspend fun clearCache() {
        measurementCache.invalidateAll()
        mdcCache.invalidateAll()
        dailyErrors.invalidateAll()
    }
}