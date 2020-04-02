package ru.nightgoat.weather.di.components

import dagger.Component
import ru.nightgoat.weather.di.modules.NetworkModule
import ru.nightgoat.weather.presentation.city.CityViewModel
import ru.nightgoat.weather.presentation.list.ListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(cityViewModel: CityViewModel)
    fun inject(listViewModel: ListViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
    }
}