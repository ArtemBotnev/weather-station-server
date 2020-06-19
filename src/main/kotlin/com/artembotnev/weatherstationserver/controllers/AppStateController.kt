package com.artembotnev.weatherstationserver.controllers

import com.artembotnev.weatherstationserver.services.AppStateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AppStateController {

    @Autowired
    lateinit var appStateService: AppStateService

    @GetMapping("/")
    fun index() = appStateService.getCurrentStateMessage()
}