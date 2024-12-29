package com.artembotnev.weather.station.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {

    @Test
    fun roundToTest() {
        val before = 34.8472390562

        assertEquals(34.84724, before.roundTo(5))
        assertEquals(34.85, before.roundTo(2))
        assertEquals(34.8, before.roundTo(1))
        assertEquals(35.0, before.roundTo(0))
    }

    @Test(expected = IllegalArgumentException::class)
    fun roundToTestException() {
        val before = 34.8472390562
        before.roundTo(0)
    }
}