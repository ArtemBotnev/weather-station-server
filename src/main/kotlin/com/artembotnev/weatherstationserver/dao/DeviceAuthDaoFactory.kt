package com.artembotnev.weatherstationserver.dao

object DeviceAuthDaoFactory {
    fun createDao(): DeviceAuthDao = DeviceAuthXmlDao()
}