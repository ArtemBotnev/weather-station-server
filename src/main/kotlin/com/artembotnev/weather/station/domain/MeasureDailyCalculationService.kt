package com.artembotnev.weather.station.domain

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory

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

    // does not work properly
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

//    private suspend fun calculateDailyValues(measurement: Measurement): List<MeasureDailyCalculation> {
//        val oldCalculation = withContext(ioDispatcher) {
//            measurement.measures
//                .map { it.sensorId }
//                .map { repository.getMeasureDailyCalculation(it) }
//        }
//
//        return measurement.measures
//            .map {
//                MeasureDailyCalculation(
//                    sensorId = it.sensorId,
//
//                )
//            }
//    }

    private fun calculateDailyValues(measurement: Measurement){
        // just stub, should be removed
        println("calculated!!!!")
    }
}