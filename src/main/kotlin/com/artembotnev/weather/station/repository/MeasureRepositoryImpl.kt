package com.artembotnev.weather.station.repository

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement
import com.artembotnev.weather.station.storage.MeasureInMemoryStorage
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

private const val MEASURE_FLOW_REPLAY = 20
private const val MEASURE_FLOW_EXTRA_BUFFER_CAPACITY = 100

internal class MeasureRepositoryImpl(private val storage: MeasureInMemoryStorage) : MeasureRepository {

    private val measureFlow = MutableSharedFlow<Measurement>(
        replay = MEASURE_FLOW_REPLAY,
        extraBufferCapacity = MEASURE_FLOW_EXTRA_BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override suspend fun setMeasurement(measurement: Measurement?) {
        measurement?.let {
            storage.setMeasurement(measurement)
            measureFlow.emit(measurement)
        }
    }

    override suspend fun getMeasurement(deviceId: Int) = storage.getMeasurement(deviceId)

    override fun observeMeasure(): SharedFlow<Measurement> = measureFlow

    override suspend fun getMeasureDailyCalculation(sensorId: String): MeasureDailyCalculation? {
        return storage.getMeasureDailyCalculation(sensorId)
    }

    override suspend fun setMeasureDailyCalculations(mdcList: List<MeasureDailyCalculation>) {
        storage.setMeasureDailyCalculations(mdcList)
    }

    override suspend fun clearCache() {
        storage.clearCache()
    }
}