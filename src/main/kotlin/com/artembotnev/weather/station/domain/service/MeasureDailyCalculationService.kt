package com.artembotnev.weather.station.domain.service

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.domain.entity.Measure
import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement
import com.artembotnev.weather.station.domain.roundTo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import kotlin.math.max
import kotlin.math.min

private const val FACTOR_START_VALUE = 1
private const val FACTOR_INCREMENT_STEP = 1
private const val ROUND_SIGNS_AFTER_POINT = 3

internal class MeasureDailyCalculationService(
    private val repository: MeasureRepository,
    private val ioDispatcher: CoroutineDispatcher,
    computationDispatcher: CoroutineDispatcher,
) {
    private val logger = LoggerFactory.getLogger("MeasureDailyCalculationServiceHandler")
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logger.error("MeasureDailyCalculationService", throwable)
    }
    private val scope = CoroutineScope(
        SupervisorJob() + computationDispatcher + exceptionHandler
    )

    private val measurementObserver = repository.observeMeasure()
        .shareIn(scope, SharingStarted.Lazily)
        .map(::calculateDailyValues)

    init {
        scope.launch {
            withContext(ioDispatcher) {
                measurementObserver.collect {
                    repository.setMeasureDailyCalculations(it)
                }
            }
        }
    }

    operator fun invoke() {
        // need for service start
    }

    private suspend fun calculateDailyValues(measurement: Measurement): List<MeasureDailyCalculation> {
        return if (measurement.isNewDay) {
            measurement.measures.mapNotNull { it.toNewDayCalculations(measurement.timestamp) }
        } else {
            measurement.measures
                .toValidMeasures()
                .map { it.toCalculations(measurement.timestamp) }
        }
    }

    private fun average(oldAverageValue: Double, newValue: Double, factor: Int): Double {
        return if (factor == 0) {
            newValue
        } else {
            ((oldAverageValue * factor + newValue) / (factor + FACTOR_INCREMENT_STEP)) roundTo ROUND_SIGNS_AFTER_POINT
        }
    }

    private fun Measure.toNewDayCalculations(timeStamp: String? = null): MeasureDailyCalculation? {
        if (sensorError) return null

        return MeasureDailyCalculation(
            sensorId = sensorId,
            maxValue = measureValue,
            minValue = measureValue,
            maxValueTime = timeStamp,
            minValueTime = timeStamp,
            averageValue = measureValue,
            factor = FACTOR_START_VALUE,
        )
    }

    private suspend fun Measure.toCalculations(timeStamp: String? = null): MeasureDailyCalculation {
        val old = repository.getMeasureDailyCalculation(sensorId)

        return MeasureDailyCalculation(
            sensorId = sensorId,
            maxValue = old?.let { max(measureValue, it.maxValue) } ?: measureValue,
            minValue = old?.let { min(measureValue, it.minValue) } ?: measureValue,
            averageValue = old?.let {
                average(
                    oldAverageValue = it.averageValue,
                    newValue = measureValue,
                    factor = it.factor,
                )
            } ?: measureValue,
            factor = old?.let { it.factor + FACTOR_INCREMENT_STEP } ?: FACTOR_START_VALUE,
            maxValueTime = old?.let {
                if (measureValue > it.maxValue) timeStamp else it.maxValueTime
            },
            minValueTime = old?.let {
                if (measureValue < it.minValue) timeStamp else it.minValueTime
            }
        )
    }

    // if sensor reading was error - do not take measurement
    private fun List<Measure>.toValidMeasures(): List<Measure> = filter { !it.sensorError }
}