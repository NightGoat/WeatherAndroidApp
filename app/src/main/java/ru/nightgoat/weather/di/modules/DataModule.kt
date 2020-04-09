package ru.nightgoat.weather.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.nightgoat.weather.data.DBRepositoryImpl
import ru.nightgoat.weather.data.db.CitiesDao
import ru.nightgoat.weather.data.db.CitiesDatabase
import ru.nightgoat.weather.domain.DBRepository
import javax.inject.Singleton

@Module
object DataModule {

    @Provides
    @Singleton
    fun provideRepository(dao: CitiesDao): DBRepository {
        return DBRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideDao(citiesDatabase: CitiesDatabase): CitiesDao {
        return citiesDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideDataBase(context: Context): CitiesDatabase = CitiesDatabase.getInstance(context)
}