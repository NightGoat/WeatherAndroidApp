package ru.nightgoat.weather.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
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
    fun insertForecast(forecast: List<ForecastEntity>): Completable
    fun deleteAllForecastForCity(cityId: Int): Completable
    fun purgeForecast(cityId: Int): Completable
}