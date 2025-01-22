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

private const val HOST_KEY = "-h"
private const val PORT_KEY = "-p"

fun main(args: Array<String>) {
    var host = HOST
    var port = PORT
    var i = 0
    while (i < args.size) {
        val key = args[i]
        val value = args[i + 1]

        when (key) {
            HOST_KEY -> host = value
            PORT_KEY -> port = value.toInt()
        }
        i += 2
    }

    embeddedServer(Netty, port = port, host = host, module = Application::module)
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
