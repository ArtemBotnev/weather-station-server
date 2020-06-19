package com.artembotnev.weatherstationserver

import com.artembotnev.weatherstationserver.dao.DeviceAuthXmlDao
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class DeviceAuthXmlDaoTest {

    @Test
    fun parserTest() {
        val dao = DeviceAuthXmlDao()
        val map = dao.getPasswordMap()

        assertEquals(2, map.keys.size)
        assertEquals("22222", map[2])
    }
}