package ru.nightgoat.weather.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.nightgoat.weather.presentation.city.CityFragment
import ru.nightgoat.weather.presentation.addCity.AddCityFragment
import ru.nightgoat.weather.presentation.list.ListFragment

@Module
abstract class MainActivityProviders {

    @ContributesAndroidInjector
    abstract fun provideCityFragment(): CityFragment

    @ContributesAndroidInjector
    abstract fun provideListFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun provideAddCityFragment(): AddCityFragment

}