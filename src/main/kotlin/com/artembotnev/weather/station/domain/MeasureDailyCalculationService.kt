package com.artembotnev.weather.station.domain

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.domain.entity.Measure
import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement
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
            measurement.measures.map { it.toNewDayCalculations() }
        } else {
            measurement.measures.map { it.toCalculations() }
        }
    }

    private fun average(oldAverageValue: Double, newValue: Double, factor: Int): Double {
        return if (factor == 0) {
            newValue
        } else {
            (oldAverageValue * factor + newValue) / (factor + FACTOR_INCREMENT_STEP)
        }
    }

    private fun Measure.toNewDayCalculations() = MeasureDailyCalculation(
        sensorId = sensorId,
        maxValue = measureValue,
        minValue = measureValue,
        averageValue = measureValue,
        factor = FACTOR_START_VALUE,
    )

    private suspend fun Measure.toCalculations(): MeasureDailyCalculation {
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
        )
    }
}