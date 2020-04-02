package ru.nightgoat.weather.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.nightgoat.weather.di.ActivityScope
import ru.nightgoat.weather.presentation.city.CityViewModel

@Module
object CityFragmentModule {

    @Provides
    @ActivityScope
    @JvmStatic
    internal fun provideCityViewModel(fragment: Fragment, factory: ViewModelProvider.NewInstanceFactory): CityViewModel {
        return ViewModelProvider(fragment, factory).get(CityViewModel::class.java)
    }

    @Provides
    @ActivityScope
    @JvmStatic
    internal fun provideViewModelFactory(): ViewModelProvider.NewInstanceFactory {
        return ViewModelProvider.NewInstanceFactory()
    }
}