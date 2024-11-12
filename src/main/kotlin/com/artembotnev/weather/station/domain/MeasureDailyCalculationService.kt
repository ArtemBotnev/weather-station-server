package com.artembotnev.weather.station.domain

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import kotlin.math.max
import kotlin.math.min

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
//                 save to repository here !!!
                }
            }
        }
    }

    operator fun invoke() {
        // need for service start
    }

    private suspend fun calculateDailyValues(measurement: Measurement): List<MeasureDailyCalculation> {
        return measurement.measures
            .map { measure ->
                val old = repository.getMeasureDailyCalculation(measure.sensorId)
                MeasureDailyCalculation(
                    sensorId = measure.sensorId,
                    maxValue = old?.let { max(measure.measureValue, it.maxValue) } ?: measure.measureValue,
                    minValue = old?.let { min(measure.measureValue, it.minValue) } ?: measure.measureValue,
                    averageValue = old?.let {
                        average(
                            oldAverageValue = it.averageValue,
                            newValue = measure.measureValue,
                            factor = it.factor,
                        )
                    } ?: measure.measureValue,
                    factor = old?.let { it.factor + 1 } ?: 1,
                )
            }
    }

    private fun average(oldAverageValue: Double, newValue: Double, factor: Int): Double {
        return if (factor == 0) {
            newValue
        } else {
            (oldAverageValue * factor + newValue) / factor + 1
        }
    }
}