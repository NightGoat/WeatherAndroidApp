package ru.nightgoat.weather.domain

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.internal.operators.flowable.FlowableToListSingle
import io.reactivex.internal.operators.single.SingleToFlowable
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.SearchEntity
import ru.nightgoat.weather.network.OpenWeatherAPI
import ru.nightgoat.weather.network.model.CityModel
import ru.nightgoat.weather.network.model.TimeGap
import ru.nightgoat.weather.utils.API_ID
import java.util.*
import java.util.concurrent.TimeUnit

class Interactor(private val repository: DBRepository, private val api: OpenWeatherAPI) {

    private var cityList: MutableList<CityEntity> = mutableListOf()

    fun getAllCities(): Flowable<MutableList<CityEntity>> {
        return repository.getAllCities().subscribeOn(Schedulers.io()).doOnNext { cityList = it }
    }

    fun deleteCity(cityEntity: CityEntity): Completable {
        return repository.deleteCity(cityEntity)
    }

    fun getCityFromApiAndPutInDB(city: String, units: String): Completable {
        return api.getCurrentWeather(
            city,
            API_ID,
            units,
            Locale.getDefault().country
        )
            .subscribeOn(Schedulers.io())
            .map { cityModel: CityModel -> cityModel.convertToCityEntity() }
            .flatMapCompletable {
                Log.d(TAG, "getCityFromApiAndPutInDB: ${it.name} position 1: ${it.position}")
                it.position = cityList.size
                Log.d(TAG, "getCityFromApiAndPutInDB: ${it.name} position 2: ${it.position}")
                repository.insertCity(it)
            }
    }

    fun updateAllFromApi(units: String): Completable {
        return repository.getAllCitiesSingle()
            .observeOn(Schedulers.io())
            .flattenAsObservable { Iterable { it.iterator() } }
            .flatMapSingle { cityEntity ->
                api.getCurrentWeatherById(
                    cityEntity.cityId,
                    API_ID,
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

//    fun getCityFromDataBaseAndUpdateFromApi(cityId: Int, units: String): Flowable<CityEntity> {
//        return repository.getCityById(cityId).toSingle()
//            .concatWith(api.getCurrentWeatherById(
//                id = cityId,
//                app_id = API_ID,
//                units = units,
//                lang = Locale.getDefault().country
//            ).map {
//                Log.d(TAG, it.name)
//                it.convertToCityEntity() }
//                .doOnSuccess {
//                    repository.insertCity(it).subscribeOn(Schedulers.io()).subscribe()
//                }
//                .doOnError {
//                    Log.e(TAG, it.message!!)
//                })
//            .subscribeOn(Schedulers.io())
//    }

    fun getCityFromDataBaseAndUpdateFromApi(cityId: Int, units: String): Single<CityEntity> {
        val a: Single<CityEntity> = repository.getCityById(cityId).toSingle()
        val b: Single<CityEntity> = api.getCurrentWeatherById(
            id = cityId,
            app_id = API_ID,
            units = units,
            lang = Locale.getDefault().country
        ).map {
            Log.d(TAG, it.name)
            it.convertToCityEntity()
        }
            .doOnSuccess {
                repository.insertCity(it).subscribeOn(Schedulers.io()).subscribe()
            }
        return b.onErrorResumeNext(a).subscribeOn(Schedulers.io())
    }

//    fun getCityFromDataBaseAndUpdateFromApi(cityId: Int, units: String): Single<CityEntity> {
//        return repository.getCityById(cityId).toSingle()
//            .subscribeOn(Schedulers.io())
//    }

    fun getSearchList(): Single<MutableList<String>> {
        return repository.getSearchList().subscribeOn(Schedulers.io())
    }

    fun insertSearchEntity(city: SearchEntity): Completable {
        return repository.insertSearchEntity(city).subscribeOn(Schedulers.io())
    }

    fun purgeSearchList(): Completable {
        return repository.purgeSearchList().subscribeOn(Schedulers.io())
    }

    fun getForecast(cityId: Int, units: String): Single<CityModel> {
        return api.getForecast(cityId, API_ID, units, Locale.getDefault().country)
            .subscribeOn(Schedulers.io())
    }

    companion object {
        @JvmStatic
        val TAG = Interactor::class.java.simpleName
    }
}


