package com.artembotnev.weatherstationserver.services.impl

import com.artembotnev.weatherstationserver.data.Cache
import com.artembotnev.weatherstationserver.data.Measurement
import com.artembotnev.weatherstationserver.data.Response
import com.artembotnev.weatherstationserver.services.MeasurementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MeasurementServiceImpl : MeasurementService {

    @Autowired
    private lateinit var cache: Cache

    override fun saveMeasurement(deviceId: Int, measurement: Measurement): Response {
        cache.lastMeasurements[deviceId] = measurement

        return Response(true, "")
    }

    override fun getLastMeasurement(deviceId: Int): Measurement? = cache.lastMeasurements[deviceId]
}