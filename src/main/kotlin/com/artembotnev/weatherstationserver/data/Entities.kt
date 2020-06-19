package com.artembotnev.weatherstationserver.data

data class Device(
        val id: Int,
        val model: String?,
        val location: String?
)

data class Measure(
        val name: String,
        val current: Int,
        val max: Int,
        val min: Int,
        val average: Float
) {
    companion object {
        const val TEMP_OUT = "temp_out"
        const val TEMP_ROOM = "temp_room"
        const val HUM_OUT = "hum_out"
        const val HUM_ROOM = "hum_room"
        const val PRESSURE = "pressure"
    }
}

data class Measurement(
        val timestamp: Long,
        val day: Int,
        val date: String?,
        val time: String,
        val measures: List<Measure>
)

class Response(
        val success: Boolean = false,
        val message: String = ""
)