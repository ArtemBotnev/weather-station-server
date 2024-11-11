package com.artembotnev.weather.station.di

import com.artembotnev.weather.station.domain.data.MeasureRepository
import com.artembotnev.weather.station.storage.MeasureInMemoryStorage
import com.artembotnev.weather.station.storage.MeasureInMemoryStorageImpl
import com.artembotnev.weather.station.repository.MeasureRepositoryImpl
import com.artembotnev.weather.station.domain.MeasureService
import com.artembotnev.weather.station.domain.MeasureDailyCalculationService
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val appModule = module {
    single(named("Computation")) {
        Dispatchers.Default
    }
    single(named("IO")) {
        Dispatchers.IO
    }

    singleOf(::MeasureInMemoryStorageImpl) { bind<MeasureInMemoryStorage>() }
    singleOf(::MeasureRepositoryImpl) { bind<MeasureRepository>() }
    singleOf(::MeasureService)
    single {
        MeasureDailyCalculationService(
            get(),
            get(named("Computation")),
            get(named("IO"))
        )
    }
}