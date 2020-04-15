package ru.nightgoat.weather.domain

import android.util.Log
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity
import ru.nightgoat.weather.network.OpenWeatherAPI
import ru.nightgoat.weather.network.model.CityModel
import ru.nightgoat.weather.utils.getHour
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class Interactor(private val repository: DBRepository, private val api: OpenWeatherAPI) {

    private var cityList: MutableList<CityEntity> = mutableListOf()

    fun getAllCities(): Flowable<MutableList<CityEntity>> {
        return repository.getAllCities().subscribeOn(Schedulers.io()).doOnNext { cityList = it }
    }

    fun deleteCity(cityEntity: CityEntity): Completable {
        return repository.deleteCity(cityEntity)
            .andThen(repository.deleteAllForecastForCity(cityEntity.cityId))
    }

    fun getCityFromApiAndPutInDB(city: String, units: String, api_key: String): Single<CityEntity> {
        return api.getCurrentWeather(
            city,
            api_key,
            units,
            Locale.getDefault().country
        )
            .subscribeOn(Schedulers.io())
            .map { cityModel: CityModel -> cityModel.convertToCityEntity() }
            .doOnSuccess{
                it.position = cityList.size
                repository.insertCity(it).subscribe()
            }
    }

    fun updateAllFromApi(units: String, API_KEY: String): Completable {
        return repository.getAllCitiesSingle()
            .observeOn(Schedulers.io())
            .flattenAsObservable { Iterable { it.iterator() } }
            .flatMapSingle { cityEntity ->
                api.getCurrentWeatherById(
                    cityEntity.cityId,
                    API_KEY,
                    units,
                    Locale.getDefault().country
                ).observeOn(Schedulers.io())
                    .doOnSuccess {
                        it.position = cityEntity.position
                    }
            }
            .map {
                it.convertToCityEntity()
            }
            .flatMapCompletable {
                repository.updateCity(it)
            }

    }

    fun swapPositionWithFirst(cityToSwap: CityEntity): Completable {
        val cityFirst = cityList.firstOrNull { it.position == 0 }
        return if (cityFirst != null) {
            cityFirst.position = cityToSwap.position
            cityToSwap.position = 0
            repository.updateCity(cityFirst)
                .andThen(repository.updateCity(cityToSwap)).subscribeOn(Schedulers.io())
        } else {
            cityToSwap.position = 0
            repository.updateCity(cityToSwap).subscribeOn(Schedulers.io())
        }
    }

    fun updateCityInDB(city: CityEntity): Completable {
        return repository.insertCity(city).subscribeOn(Schedulers.io())
    }

    fun getCityFromDataBaseAndUpdateFromApi(cityId: Int, units: String, API_KEY : String): Flowable<CityEntity> {
        return repository.getCityById(cityId)
            .concatWith(api.getCurrentWeatherById(
                id = cityId,
                app_id = API_KEY,
                units = units,
                lang = Locale.getDefault().country
            ).map {
                it.convertToCityEntity()
            }
                .toMaybe()
                .timeout(10, TimeUnit.SECONDS)
                .doOnSuccess {
                    repository.insertCity(it).subscribeOn(Schedulers.io()).subscribe()
                }
                .doOnError {
                    Log.e(TAG, it.message!!)
                })
            .subscribeOn(Schedulers.io())
    }

    fun getSearchList(): Single<MutableList<String>> {
        return repository.getSearchList().subscribeOn(Schedulers.io())
    }

    fun insertSearchEntity(city: SearchEntity): Completable {
        return repository.insertSearchEntity(city).subscribeOn(Schedulers.io())
    }

    fun purgeSearchList(): Completable {
        return repository.purgeSearchList().subscribeOn(Schedulers.io())
    }

    fun getForecast(cityId: Int): Flowable<MutableList<ForecastEntity>> {
        return repository.getForecast(cityId).subscribeOn(Schedulers.io())
    }

    fun updateForecast(cityId: Int, units: String, API_KEY: String): Disposable {
        return api.getForecast(
            id = cityId,
            app_id = API_KEY,
            units = units,
            lang = Locale.getDefault().country)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    for (gap in it.list) {
                        if (getHour(gap.dt*1000) == 12) {
                            repository.insertForecast(
                                ForecastEntity(
                                    it.city.cityId,
                                    it.city.name,
                                    gap.dt,
                                    gap.main.temp.roundToInt(),
                                    gap.weather[0].id,
                                    it.city.sunrise,
                                    it.city.sunset
                                )
                            ).subscribe()
                        }
                    }
                }, {
                    Log.e(TAG, it.message.toString())
                })
    }

    fun purgeForecast(cityId: Int) : Completable{
        return repository.purgeForecast(cityId)
    }

    companion object {
        @JvmStatic
        val TAG = Interactor::class.java.simpleName
    }
}


