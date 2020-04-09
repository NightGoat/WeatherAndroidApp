package ru.nightgoat.weather.di.modules

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import ru.nightgoat.weather.di.builder.ViewModelBuilder
import javax.inject.Singleton

@Module(includes = [ViewModelBuilder::class])
class ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext
}