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

//    override var measurement: Measurement?
//        get() = Measurement(
//            timestamp = "2024-09-09T17:09:31+00:00",
//            timeZoneHours = 3,
//            device = Device(
//                id = 0,
//                type = "Controller",
//                name = "Test controller",
//                location = "My sweet home"
//            ),
//            measures = listOf(
//                Measure(
//                    sensorId = "Sensor_0",
//                    sensorName = "cool sensor",
//                    measureName = "temperature",
//                    sensorPlace = "room"
//                    measureUnit = "Celsius",
//                    measureValue = 27.23
//                )
//            )
//        )

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

    override suspend fun getMeasureDailyCalculation(sensorId: String): MeasureDailyCalculation {
        TODO("Not yet implemented")
    }
}