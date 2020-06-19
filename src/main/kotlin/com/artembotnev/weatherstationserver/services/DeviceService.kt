package com.artembotnev.weatherstationserver.services

import com.artembotnev.weatherstationserver.data.Device
import com.artembotnev.weatherstationserver.data.Response

interface DeviceService {
    /**
     * saves measurement received from device
     *
     * @param device - new device
     * @return response with operation status
     */
    fun addDevice(device: Device): Response

    /**
     * returns device with particular id
     *
     * @param deviceId - id of device
     * @return device with particular id or null if device with such id not found
     */
    fun getDevice(deviceId: Int): Device?
}