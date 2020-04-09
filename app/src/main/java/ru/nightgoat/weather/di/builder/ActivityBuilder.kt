package ru.nightgoat.weather.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.nightgoat.weather.presentation.MainActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityProviders::class])
    abstract fun bindMainActivity(): MainActivity
}