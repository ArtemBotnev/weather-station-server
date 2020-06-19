package com.artembotnev.weatherstationserver.services

import com.artembotnev.weatherstationserver.data.Measurement
import com.artembotnev.weatherstationserver.data.Response

interface MeasurementService {
    /**
     * saves measurement received from device
     *
     * @param deviceId - id of device from which measurement is taken
     * @param measurement - obtained measurement
     * @return response with operation status
     */
    fun saveMeasurement(deviceId: Int, measurement: Measurement): Response

    /**
     * returns last measurement from devices with particular id
     *
     * @param deviceId - id of device from which measurement is taken
     * @return last measurement from device or null if there are no measurement from this device
     */
    fun getLastMeasurement(deviceId: Int): Measurement?
}