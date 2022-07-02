package ru.nightgoat.weather.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nightgoat.weather.network.model.CityModel

interface OpenWeatherAPI {

    @GET("data/2.5/weather?")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("APPID") appId: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Single<CityModel>

    @GET("data/2.5/weather?")
    fun getCurrentWeatherById(
        @Query("id") id: Int,
        @Query("APPID") appId: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Single<CityModel>

    @GET("data/2.5/forecast?")
    fun getForecast(
        @Query("id") id: Int,
        @Query("APPID") appId: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Single<CityModel>
}