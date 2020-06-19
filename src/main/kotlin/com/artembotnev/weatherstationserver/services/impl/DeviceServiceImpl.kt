package com.artembotnev.weatherstationserver.services.impl

import com.artembotnev.weatherstationserver.data.Cache
import com.artembotnev.weatherstationserver.data.Device
import com.artembotnev.weatherstationserver.data.Response
import com.artembotnev.weatherstationserver.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeviceServiceImpl : DeviceService {

    @Autowired
    private lateinit var cache: Cache

    override fun addDevice(device: Device): Response {
        cache.deviceMap[device.id] = device

        return Response(true, "")
    }

    override fun getDevice(deviceId: Int): Device? = cache.deviceMap[deviceId]
}