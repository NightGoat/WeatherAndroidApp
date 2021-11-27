package ru.nightgoat.weather.domain


import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity

interface IInteractor {

    fun getAllCities(): Flowable<MutableList<CityEntity>>

    fun deleteCity(cityEntity: CityEntity): Completable

    fun getCityFromApiAndPutInDB(city: String, units: String, api_key: String): Single<CityEntity>

    fun updateAllFromApi(units: String, API_KEY: String): Completable

    fun swapPositionWithFirst(cityToSwap: CityEntity): Completable

    fun updateCityInDB(city: CityEntity): Completable

    fun getCityFromDataBaseAndUpdateFromApi(cityId: Int, units: String, API_KEY : String): Flowable<CityEntity>

    fun getSearchList(): Single<MutableList<String>>

    fun insertSearchEntity(city: SearchEntity): Completable

    fun purgeSearchList(): Completable

    fun getForecast(cityId: Int): Flowable<MutableList<ForecastEntity>>

    fun updateForecast(cityId: Int, units: String, API_KEY: String): Disposable

    fun purgeForecast(cityId: Int) : Completable
}