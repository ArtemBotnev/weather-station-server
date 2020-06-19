package com.artembotnev.weatherstationserver.controllers

import com.artembotnev.weatherstationserver.data.Device
import com.artembotnev.weatherstationserver.data.Response
import com.artembotnev.weatherstationserver.services.DeviceService
import com.artembotnev.weatherstationserver.services.VerificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/device")
@ResponseBody
class DeviceController {

    @Autowired
    private lateinit var deviceService: DeviceService
    @Autowired
    private lateinit var verificationService: VerificationService

    @PostMapping("/v1.0/add/{id}", consumes = ["application/json;charset=utf-8"])
    fun postDevice(
            @RequestHeader("token") token: String,
            @PathVariable id: Int,
            @RequestBody device: Device
    ): ResponseEntity<Response> {
        if (!verificationService.checkToken(device.id, token)) {
            return ResponseEntity(Response(false, "Wrong token"), HttpStatus.UNAUTHORIZED)
        }

        return ResponseEntity(deviceService.addDevice(device), HttpStatus.OK)
    }

    @GetMapping("/v1.0", produces = ["application/json;charset=utf-8"])
    fun getDevice(@RequestParam("id") id: Int): ResponseEntity<*> {
        return deviceService.getDevice(id)?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(
                Response(false, "Device with id $id not found"),
                HttpStatus.OK
        )
    }
}