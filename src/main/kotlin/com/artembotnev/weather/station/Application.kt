package com.artembotnev.weather.station

import com.artembotnev.weather.station.di.appModule
import com.artembotnev.weather.station.plugins.configureRouting
import com.artembotnev.weather.station.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

private const val PORT = 8080
private const val HOST = "0.0.0.0"

fun main() {
    embeddedServer(Netty, port = PORT, host = HOST, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}
