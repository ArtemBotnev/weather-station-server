package com.artembotnev.weatherstationserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Created by Artem Botnev on June 10 2020
 */
@SpringBootApplication
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
