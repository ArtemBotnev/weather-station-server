package com.artembotnev.weatherstationserver

import com.artembotnev.weatherstationserver.data.Measure
import com.artembotnev.weatherstationserver.data.Measurement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random


class MeasurementControllerIntegrationTest {

    @Test
    fun addMeasurementAndCheck() {
        val tokenKey = "token"
        val measurement = createMeasurement()
        val token = "12sdfksjdi323434"

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.add(tokenKey, token)

        val entity = HttpEntity(measurement, headers)
        val template = RestTemplate()

        val receivedMeasurement = template.exchange(
                "$ROOT$ADD/1" ,
                HttpMethod.POST,
                entity,
                Measurement::class.java
        )

        assertEquals(token, receivedMeasurement.headers[tokenKey])
        assertNotNull(receivedMeasurement)
        assertEquals(measurement, receivedMeasurement.body)
    }

    private fun createMeasurement(): Measurement {
        val measures = listOf(
                createMeasure(Measure.TEMP_OUT, -35, 35),
                createMeasure(Measure.TEMP_ROOM, 16, 35),
                createMeasure(Measure.HUM_OUT, 10, 100),
                createMeasure(Measure.HUM_ROOM, 10, 100),
                createMeasure(Measure.PRESSURE, 715, 775)
        )

        return Measurement(
                System.currentTimeMillis(),
                3,
                LocalDate.now().toString(),
                LocalTime.now().toString(),
                measures
        )
    }

    private fun createMeasure(name: String, possibleMin: Int, possibleMax: Int): Measure {
        val first = Random.nextInt(possibleMin, possibleMax)
        val second = Random.nextInt(possibleMin, possibleMax)
        val min = first.coerceAtMost(second)
        val max = first.coerceAtLeast(second)

        return Measure(
                name,
                Random.nextInt(min, max),
                max,
                min,
                Random.nextDouble(min.toDouble(), max.toDouble()).toFloat()
        )
    }

    companion object {
        private const val ROOT = "http://localhost:8080/measurement"
        private const val ADD = "/add"
    }
}