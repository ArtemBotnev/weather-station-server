package com.artembotnev.weatherstationserver.services.impl

import com.artembotnev.weatherstationserver.dao.DeviceAuthDaoFactory
import com.artembotnev.weatherstationserver.data.Cache
import com.artembotnev.weatherstationserver.services.VerificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import toHexString
import java.security.MessageDigest

@Service
class VerificationServiceImpl : VerificationService {

    @Autowired
    private lateinit var cache: Cache

    override fun checkToken(deviceId: Int, token: String): Boolean {
        if (cache.tokenMap == null) createTokenMap()

        return token == cache.tokenMap!![deviceId]
    }

    private fun createTokenMap() {
        val passMap = DeviceAuthDaoFactory.createDao().getPasswordMap()
        cache.tokenMap = passMap.toSHA256()
    }

    private fun <T> Map<T, String>.toSHA256(): Map<T, String> {
        val resultMap = mutableMapOf<T, String>()
        val digest = MessageDigest.getInstance(ALGORITHM)
        entries.forEach {
            val shaString = digest.digest(it.value.toByteArray()).toHexString()
            resultMap[it.key] = shaString
        }

        return resultMap
    }

    companion object {
        private const val ALGORITHM = "SHA-256"
    }
}