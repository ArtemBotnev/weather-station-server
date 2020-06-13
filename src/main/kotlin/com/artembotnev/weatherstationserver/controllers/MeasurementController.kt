package com.artembotnev.weatherstationserver.controllers

import com.artembotnev.weatherstationserver.data.Measurement
import com.artembotnev.weatherstationserver.data.Response
import org.springframework.web.bind.annotation.*

/**
 * Created by Artem Botnev on June 10 2020
 */
@RestController
@RequestMapping("/measurement")
@ResponseBody
class MeasurementController {

    @PostMapping("/v1.0/add/{id}", consumes = ["application/json;charset=utf-8"])
    fun postMeasurement(
            @RequestHeader("token") token: String,
            @PathVariable id: String,
            @RequestBody measurement: Measurement
    ): Response = Response(true, "")
}