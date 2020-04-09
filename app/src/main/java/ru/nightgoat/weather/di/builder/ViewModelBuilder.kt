package ru.nightgoat.weather.di.builder

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.nightgoat.weather.presentation.ViewModelFactory

@Module(includes = [ViewModelModule::class])
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory)
            : ViewModelProvider.Factory
}