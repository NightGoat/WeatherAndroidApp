package ru.nightgoat.weather.presentation

import androidx.lifecycle.ViewModel
import ru.nightgoat.weather.di.components.DaggerViewModelInjector
import ru.nightgoat.weather.di.modules.NetworkModule
import ru.nightgoat.weather.di.components.ViewModelInjector
import ru.nightgoat.weather.presentation.city.CityViewModel
import ru.nightgoat.weather.presentation.list.ListViewModel

abstract class BaseViewModel: ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is CityViewModel -> injector.inject(this)
            is ListViewModel -> injector.inject(this)
        }
    }
}