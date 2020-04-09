package ru.nightgoat.weather.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.nightgoat.weather.data.entity.CityEntity

interface DBRepository {

    fun getAllCities(): Flowable<MutableList<CityEntity>>
    fun getAllCitiesSingle(): Single<MutableList<CityEntity>>
    fun getCity(name: String): Maybe<CityEntity>
    fun getCityById(cityId: Int): Maybe<CityEntity>
    fun insertCity(city: CityEntity): Completable
    fun deleteCity(city: CityEntity): Completable
    fun updateCity(city: CityEntity): Completable
}