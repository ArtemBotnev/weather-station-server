package com.artembotnev.weather.station.domain.service

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.domain.entity.Measure
import com.artembotnev.weather.station.domain.entity.Measurement
import com.artembotnev.weather.station.domain.entity.analytics.DeviceDailyErrors
import com.artembotnev.weather.station.domain.entity.analytics.SensorDailyErrors
import com.artembotnev.weather.station.domain.percentOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

private const val START_MEASURE_COUNT = 1

internal class DeviceDailyErrorsService(
    private val repository: MeasureRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val timeService: DateTimeService,
) {
    private val logger = LoggerFactory.getLogger("DeviceDailyErrorsServiceHandler")
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logger.error("DeviceDailyErrorsService", throwable)
    }
    private val scope = CoroutineScope(
        SupervisorJob() + ioDispatcher + exceptionHandler
    )

    private val errorObserver = repository.observeMeasure()
        .shareIn(scope, SharingStarted.Lazily)
        .map(::processData)

    init {
        scope.launch {
            withContext(ioDispatcher) {
                errorObserver.collect {
                    repository.setDeviceDailyErrors(it)
                }
            }
        }
    }

    operator fun invoke() {
        // need for service start
    }

    suspend fun getDeviceDailyErrors(deviceId: Int): DeviceDailyErrors? {
        return repository.getDeviceDailyErrors(deviceId)
            ?.addDateInfo()
    }

    suspend fun getDeviceListDailyErrors(): List<DeviceDailyErrors> {
        return repository.getDeviceListDailyErrors()
            .map { it.addDateInfo() }
    }

    private fun DeviceDailyErrors.addDateInfo() = this.copy(
        startAnalysisTime = timeService.getDateTimePack(timeZone = timeZoneHours, dateTime = startTime)
            .timestamp,
        lastUpdateAnalysisTime = timeService.getDateTimePack(timeZone = timeZoneHours, dateTime = lastUpdateTime)
            .timestamp,
        analysisDuration = timeService.getDurationStringInHours(startTime, lastUpdateTime)
    )

    private suspend fun processData(measurement: Measurement): DeviceDailyErrors {
        val dailyErrors = repository.getDeviceDailyErrors(measurement.device.id)
        val isNew = measurement.isNewDay || dailyErrors == null
        val currentTime = timeService.getCurrentDateTime()

        return DeviceDailyErrors(
            deviceId = measurement.device.id,
            deviceName = measurement.device.name,
            sensorErrorList = measurement.measures.map {
                if (isNew) {
                    it.toNewDayErrors()
                } else {
                    dailyErrors?.let { errors -> it.toErrors(errors) } ?: it.toNewDayErrors()
                }
            },
            startTime = if (isNew) currentTime else dailyErrors?.startTime ?: currentTime,
            lastUpdateTime = currentTime,
            timeZoneHours = measurement.timeZoneHours,
        )
    }

    private fun Measure.toNewDayErrors(): SensorDailyErrors {
        val errorCount = if (sensorError) START_MEASURE_COUNT else 0

        return SensorDailyErrors(
            sensorId = sensorId,
            sensorName = sensorName,
            totalMeasures = START_MEASURE_COUNT,
            errorCount = errorCount,
            errorPercent = errorCount percentOf START_MEASURE_COUNT,
        )
    }

    private fun Measure.toErrors(last: DeviceDailyErrors): SensorDailyErrors {
        val sensorErrors = last.sensorErrorList
            .firstOrNull { it.sensorId == sensorId } ?: toNewDayErrors()

        val errorCount = if (sensorError) sensorErrors.errorCount + 1 else sensorErrors.errorCount
        val totalCount = sensorErrors.totalMeasures + 1
        val errorPercent = errorCount percentOf totalCount

        return SensorDailyErrors(
            sensorId = sensorId,
            sensorName = sensorName,
            totalMeasures = totalCount,
            errorCount = errorCount,
            errorPercent = errorPercent,
        )
    }
}