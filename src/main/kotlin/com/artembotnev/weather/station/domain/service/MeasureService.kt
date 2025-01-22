package com.artembotnev.weather.station.domain.service

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.domain.entity.Measure
import com.artembotnev.weather.station.domain.entity.MeasureDailyCalculation
import com.artembotnev.weather.station.domain.entity.Measurement


internal class MeasureService(
    private val dataTimeService: DateTimeService,
    private val repository: MeasureRepository
) {

    suspend fun setMeasurement(measurement: Measurement?) {
        if (measurement == null) return

        val dateTimePack = dataTimeService.getDateTimePack(measurement.timeZoneHours)
        val lastMeasurement = repository.getMeasurement(measurement.device.id)
        val isNewDay = dateTimePack.dayOfMonth != lastMeasurement?.currentDay

        val updatedMeasurement = measurement.copy(
            timestamp = measurement.timestamp ?: dateTimePack.timestamp,
            currentDay = dateTimePack.dayOfMonth,
            isNewDay = isNewDay,
            measures = measurement.measures,
        )

        repository.setMeasurement(updatedMeasurement)
    }

    suspend fun getMeasurement(deviceId: Int, showAdditionalData: Boolean = false): Measurement? {
        val measurement = repository.getMeasurement(deviceId) ?: return null
        if (showAdditionalData) {
            val measuresWithCalculations = measurement.measures
                .map { measure ->
                    measure.copy(
                        dailyCalculation = repository
                            .getMeasureDailyCalculation(measure.sensorId)
                            ?.toDailyCalculation(),
                    )
                }

            return measurement.copy(measures = measuresWithCalculations)
        } else {
            return measurement
        }
    }

    suspend fun clearCache() {
        repository.clearCache()
    }

    private fun MeasureDailyCalculation.toDailyCalculation(): Measure.DailyCalculation {
        return Measure.DailyCalculation(
            maxValue = maxValue,
            minValue = minValue,
            averageValue = averageValue,
            maxValueTime = maxValueTime,
            minValueTime = minValueTime
        )
    }
}