package com.artembotnev.weatherstationserver.controllers

import com.artembotnev.weatherstationserver.data.Device
import org.springframework.web.bind.annotation.*

/**
 * Created by Artem Botnev on June 12 2020
 */
@RestController
@RequestMapping("/device")
class DeviceController {

    @PostMapping("/add", consumes = ["application/json;charset=utf-8"])
    fun postDevice(
            @RequestHeader("superToken") superToken: String,
            @RequestBody device: Device
    ) {

    }
}