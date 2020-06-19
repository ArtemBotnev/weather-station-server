package com.artembotnev.weatherstationserver.dao

interface DeviceAuthDao {

    /**
     * @return map with device id as a key and device password as a value
     */
    fun getPasswordMap(): Map<Int, String>
}