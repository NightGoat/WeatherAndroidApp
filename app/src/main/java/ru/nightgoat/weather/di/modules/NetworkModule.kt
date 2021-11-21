package ru.nightgoat.weather.di.modules

import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.nightgoat.weather.core.utils.BASE_URL
import ru.nightgoat.weather.network.OpenWeatherAPI
import javax.inject.Singleton

@Module
object NetworkModule{

    @Provides
    @Singleton
    internal fun provideAPI(retrofit: Retrofit): OpenWeatherAPI{
        return retrofit.create(OpenWeatherAPI::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}