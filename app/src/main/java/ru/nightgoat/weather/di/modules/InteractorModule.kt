package ru.nightgoat.weather.di.modules

import dagger.Module
import dagger.Provides
import ru.nightgoat.weather.domain.DBRepository
import ru.nightgoat.weather.domain.IInteractor
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.network.OpenWeatherAPI
import javax.inject.Singleton

@Module
object InteractorModule {

    @Provides
    @Singleton
    fun provideInteractor(repository: DBRepository, api: OpenWeatherAPI): IInteractor {
        return Interactor(repository, api)
    }
}