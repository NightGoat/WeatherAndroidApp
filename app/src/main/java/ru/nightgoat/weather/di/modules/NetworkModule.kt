package ru.nightgoat.weather.di.modules

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.nightgoat.weather.network.OpenWeatherAPI
import ru.nightgoat.weather.utils.BASE_URL
import javax.inject.Singleton

@Module
object NetworkModule{

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideAPI(retrofit: Retrofit): OpenWeatherAPI{
        return retrofit.create(OpenWeatherAPI::class.java)
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}