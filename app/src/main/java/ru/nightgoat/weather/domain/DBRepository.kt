package ru.nightgoat.weather.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity

interface DBRepository {

    fun getAllCities(): Flowable<MutableList<CityEntity>>
    fun getAllCitiesSingle(): Single<MutableList<CityEntity>>
    fun getCity(name: String): Maybe<CityEntity>
    fun getCityById(cityId: Int): Maybe<CityEntity>
    fun insertCity(city: CityEntity): Completable
    fun deleteCity(city: CityEntity): Completable
    fun updateCity(city: CityEntity): Completable

    fun getSearchList(): Single<MutableList<String>>
    fun insertSearchEntity(city: SearchEntity): Completable
    fun purgeSearchList(): Completable

    fun getForecast(cityId: Int): Flowable<MutableList<ForecastEntity>>
    fun insertForecast(forecast: ForecastEntity): Completable
    fun deleteAllForecastForCity(cityId: Int): Completable
    fun purgeForecast(cityId: Int): Completable
}