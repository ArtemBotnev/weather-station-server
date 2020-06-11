package com.artembotnev.weatherstationserver.controllers

import com.artembotnev.weatherstationserver.data.Measurement
import org.springframework.web.bind.annotation.*

/**
 * Created by Artem Botnev on June 10 2020
 */
@RestController
@RequestMapping("/measurement")
class MeasurementController {

    @PostMapping("/add/{id}", consumes = ["application/json;charset=utf-8"])
    fun postMeasurement(
            @RequestHeader("token") token: String,
            @PathVariable id: String,
            @RequestBody measurement: Measurement
    ) {

    }
}