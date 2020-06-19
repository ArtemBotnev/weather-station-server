package com.artembotnev.weatherstationserver.controllers

import com.artembotnev.weatherstationserver.data.Measurement
import com.artembotnev.weatherstationserver.data.Response
import com.artembotnev.weatherstationserver.services.MeasurementService
import com.artembotnev.weatherstationserver.services.VerificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/measurement")
@ResponseBody
class MeasurementController {

    @Autowired
    private lateinit var measurementService: MeasurementService
    @Autowired
    private lateinit var verificationService: VerificationService

    @PostMapping("/v1.0/add/{id}", consumes = ["application/json;charset=utf-8"])
    fun postMeasurement(
            @RequestHeader("token") token: String,
            @PathVariable id: Int,
            @RequestBody measurement: Measurement
    ): ResponseEntity<Response> {
        if (!verificationService.checkToken(id, token)) {
            return ResponseEntity(Response(false, "Wrong token"), HttpStatus.UNAUTHORIZED)
        }

        return ResponseEntity(measurementService.saveMeasurement(id, measurement), HttpStatus.OK)
    }

    @GetMapping("/v1.0", produces = ["application/json;charset=utf-8"])
    fun getLastMeasurement(@RequestParam("id") id: Int): ResponseEntity<*> {
        return measurementService.getLastMeasurement(id)?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(
                Response(false, "Measurements from device with id $id not found"),
                HttpStatus.OK
        )
    }
}