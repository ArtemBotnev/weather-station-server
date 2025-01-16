package com.artembotnev.weather.station.domain

import kotlin.jvm.Throws
import kotlin.math.pow
import kotlin.math.round

@Throws(IllegalArgumentException::class)
internal infix fun Double.roundTo(signsAfterPoint: Int): Double {
    if (signsAfterPoint < 0) throw IllegalArgumentException("signsAfterPoint must be positive")

    val multiplier = 10.0.pow(signsAfterPoint)
    return round(this * multiplier) / multiplier
}

internal infix fun Int.percentOf(total: Int): Double = (this * 100.0 / total) roundTo 1