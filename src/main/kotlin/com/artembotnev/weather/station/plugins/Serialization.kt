package com.artembotnev.weather.station.plugins

import com.artembotnev.weather.station.domain.MeasureDailyCalculationService
import com.artembotnev.weather.station.domain.MeasureService
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

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
            }
        )
    }
    routing {
        get("/measurement") {
//            TODO: add parameter
            measureService.getMeasurement(0)?.let {
                call.respond(Json.encodeToString(serializer<Measurement>(), it))
            } ?: call.respondText("Measurement for this device not found", status = HttpStatusCode.NotFound)
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
    }
}
