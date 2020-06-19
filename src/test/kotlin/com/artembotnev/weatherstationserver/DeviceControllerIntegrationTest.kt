package com.artembotnev.weatherstationserver

import com.artembotnev.weatherstationserver.data.Device
import com.artembotnev.weatherstationserver.data.Response
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class DeviceControllerIntegrationTest {

    @Test
    fun addDeviceAndCheck() {
        val tokenKey = "token"
        val token = "d17f25ecfbcc7857f7bebea469308be0b2580943e96d13a3ad98a13675c4bfc2"
        val deviceId = 1
        val device = createDevice(deviceId)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.add(tokenKey, token)

        val entity = HttpEntity(device, headers)

        val response = RestTemplate().postForEntity(
                "$ROOT$V_1_0$ADD/$deviceId",
                entity,
                Response::class.java
        )

        Assertions.assertNotNull(response)
        Assertions.assertEquals("OK", response.statusCode.reasonPhrase)
        Assertions.assertNotNull(response.body)
        Assertions.assertTrue(response.body!!.success)

        // get method
        val getResponse = RestTemplate().getForEntity(
                "$ROOT$V_1_0?id=$deviceId",
                Device::class.java
        )

        Assertions.assertNotNull(getResponse)
        Assertions.assertEquals("OK", getResponse.statusCode.reasonPhrase)
        Assertions.assertNotNull(getResponse.body)
        Assertions.assertEquals(device, getResponse.body)
    }

    private fun createDevice(id: Int) = Device(id, "ESP32", "Russian")

    companion object {
        private const val ROOT = "http://localhost:8080/device"
        private const val V_1_0 = "/v1.0"
        private const val ADD = "/add"
    }
}