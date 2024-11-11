package com.artembotnev.weather.station.plugins

import com.artembotnev.weather.station.domain.entity.Device
import com.artembotnev.weather.station.domain.entity.Measure
import com.artembotnev.weather.station.domain.entity.Measurement
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationTest {

    @Test
    fun testMeasurementSerialization() {
        val measurement = createMeasurement()
        val serializer = serializer<Measurement>()
        println(createMeasurementJson())
        assertEquals(createMeasurementJson(), Json.encodeToString(serializer, measurement))
    }

    private fun createMeasurement() = Measurement(
        timestamp = "2024-09-09T17:09:31+00:00",
        timeZoneHours = 3,
        device = Device(
            id = 0,
            type = "Controller",
            name = "Test controller",
            location = "My sweet home"
        ),
        measures = listOf(
            Measure(
                sensorId = "Sensor_0",
                sensorName = "cool sensor",
                measureName = "temperature",
                measureUnit = "Celsius",
                measureValue = 27.23,
                sensorPlace = "Just place"
            )
        )
    )

    private fun createMeasurementJson() = "{" +
            "\"timestamp\":\"2024-09-09T17:09:31+00:00\"," +
            "\"timeZoneHours\":3," +
            "\"device\":{\"id\":0,\"type\":\"Controller\",\"name\":\"Test controller\",\"location\":\"My sweet home\"}," +
            "\"measures\":[{\"sensorId\":\"Sensor_0\",\"sensorName\":\"cool sensor\",\"measureName\":\"temperature\",\"measureValue\":27.23,\"measureUnit\":\"Celsius\"}]}"
}