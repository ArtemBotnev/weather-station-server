package com.artembotnev.weather.station.plugins

import com.artembotnev.weather.station.domain.DeviceService
import com.artembotnev.weather.station.domain.MeasureDailyCalculationService
import com.artembotnev.weather.station.domain.MeasureService
import com.artembotnev.weather.station.domain.entity.Device
import com.artembotnev.weather.station.domain.entity.Measurement
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.ktor.ext.inject

fun Application.configureSerialization() {
    val measureService by inject<MeasureService>()
    val dailyCalculations by inject<MeasureDailyCalculationService>()
    val deviceService by inject<DeviceService>()

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
            }
        )
    }
    routing {
        get("/last_measurement/{device_id}") {
            val deviceId = call.parameters["device_id"]
                ?: return@get call.respondText("Device id not found", status = HttpStatusCode.NotFound)
            val id = try {
                deviceId.toInt()
            } catch (e: NumberFormatException) {
                this@configureSerialization.log.error(e.message)
                return@get call.respondText("Wrong device id format, use integer", status = HttpStatusCode.BadRequest)
            }
            val showAdditionalData = call.request.queryParameters["additional"] == "true"
            measureService.getMeasurement(id, showAdditionalData)?.let {
                call.respond(Json.encodeToString(serializer<Measurement>(), it))
            } ?: call.respondText("Measurement for this device not found", status = HttpStatusCode.NotFound)
        }
        get("/devices") {
            try {
                val devices = deviceService.getDevices()
                return@get call.respond(Json.encodeToString(serializer<List<Device>>(), devices))
            } catch (t: Throwable) {
                this@configureSerialization.log.error(t.message)
            }
        }
        post("/send_measurement")  {
            val measurement = call.receive<Measurement>()
            try {
                measureService.setMeasurement(measurement)
                dailyCalculations()
                call.respondText("Measurement stored correctly", status = HttpStatusCode.Created)
            } catch (t: Throwable) {
                this@configureSerialization.log.error(t.message)
            }
        }
        post("/clear_cache") {
            measureService.clearCache()
            call.respondText("Cache was cleared successfully", status = HttpStatusCode.OK)
        }
    }
}
