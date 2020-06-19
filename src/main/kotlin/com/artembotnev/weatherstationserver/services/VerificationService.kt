package com.artembotnev.weatherstationserver.services

interface VerificationService {

    fun checkToken(deviceId: Int, token: String): Boolean
}