package com.artembotnev.weather.station.storage

import com.artembotnev.weather.station.domain.entity.Device
import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement
import com.artembotnev.weather.station.domain.entity.analytics.DeviceDailyErrors

internal interface MeasureInMemoryStorage {
    suspend fun setMeasurement(measurement: Measurement)
    suspend fun getMeasurement(deviceId: Int): Measurement?
    suspend fun getMeasureDailyCalculation(sensorId: String): MeasureDailyCalculation?
    suspend fun setMeasureDailyCalculations(mdcList: List<MeasureDailyCalculation>)
    suspend fun getDevices(): List<Device>
    suspend fun setDeviceDailyErrors(deviceDailyErrors: DeviceDailyErrors)
    suspend fun getDeviceDailyErrors(deviceId: Int): DeviceDailyErrors?
    suspend fun getDeviceListDailyErrors(): List<DeviceDailyErrors>
    suspend fun clearCache()
}