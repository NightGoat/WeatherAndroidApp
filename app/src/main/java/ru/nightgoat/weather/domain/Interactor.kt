package ru.nightgoat.weather.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity
import ru.nightgoat.weather.network.OpenWeatherAPI
import ru.nightgoat.weather.network.model.CityModel
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class Interactor(private val repository: DBRepository, private val api: OpenWeatherAPI) :
    IInteractor {

    private var cityList: MutableList<CityEntity> = mutableListOf()

    private val defaultSheduler = Schedulers.io()

    override fun getAllCities(): Flowable<MutableList<CityEntity>> {
        return repository.getAllCities().subscribeOn(Schedulers.io()).doOnNext { cityList = it }
    }

    override fun deleteCity(cityEntity: CityEntity): Completable {
        return repository.deleteCity(cityEntity)
            .andThen(repository.deleteAllForecastForCity(cityEntity.cityId))
    }

    override fun getCityFromApiAndPutInDB(
        city: String,
        units: String,
        api_key: String
    ): Single<CityEntity> {
        return api.getCurrentWeather(
            city,
            api_key,
            units,
            Locale.getDefault().country
        )
            .subscribeOn(defaultSheduler)
            .map { cityModel: CityModel -> cityModel.convertToCityEntity() }
            .doOnSuccess {
                it.position = cityList.size
                repository.insertCity(it).subscribe()
            }
    }

    override fun updateAllFromApi(units: String, API_KEY: String): Completable {
        return repository.getAllCitiesSingle()
            .observeOn(Schedulers.io())
            .flattenAsObservable { Iterable { it.iterator() } }
            .flatMapSingle { cityEntity ->
                api.getCurrentWeatherById(
                    cityEntity.cityId,
                    API_KEY,
                    units,
                    Locale.getDefault().country
                ).observeOn(defaultSheduler)
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

    override fun swapPositionWithFirst(cityToSwap: CityEntity): Completable {
        val cityFirst = cityList.firstOrNull { it.position == DEFAULT_POSITION }
        return if (cityFirst != null) {
            cityFirst.position = cityToSwap.position
            cityToSwap.position = DEFAULT_POSITION
            repository.updateCity(cityFirst)
                .andThen(repository.updateCity(cityToSwap)).subscribeOn(defaultSheduler)
        } else {
            cityToSwap.position = DEFAULT_POSITION
            repository.updateCity(cityToSwap).subscribeOn(defaultSheduler)
        }
    }

    override fun updateCityInDB(city: CityEntity): Completable {
        return repository.insertCity(city).subscribeOn(defaultSheduler)
    }

    override fun getCityFromDataBaseAndUpdateFromApi(
        cityId: Int,
        units: String,
        API_KEY: String
    ): Flowable<CityEntity> {
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
                .timeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .doOnSuccess {
                    repository.insertCity(it).subscribeOn(defaultSheduler).subscribe()
                }
                .doOnError {
                    Timber.e(it)
                })
            .subscribeOn(defaultSheduler)
    }

    override fun getSearchList(): Single<MutableList<String>> {
        return repository.getSearchList().subscribeOn(defaultSheduler)
    }

    override fun insertSearchEntity(city: SearchEntity): Completable {
        return repository.insertSearchEntity(city).subscribeOn(defaultSheduler)
    }

    override fun purgeSearchList(): Completable {
        return repository.purgeSearchList().subscribeOn(defaultSheduler)
    }

    override fun getForecast(cityId: Int): Flowable<MutableList<ForecastEntity>> {
        return repository.getForecast(cityId).subscribeOn(defaultSheduler)
    }

    override fun updateForecast(cityId: Int, units: String, API_KEY: String): Disposable {
        return api.getForecast(
            id = cityId,
            app_id = API_KEY,
            units = units,
            lang = Locale.getDefault().country
        )
            .subscribeOn(defaultSheduler)
            .subscribe(
                {
                    for (gap in it.list) {
                        if (gap.dtTxt.contains(MIDDLE_DAY_STRING)) {
                            repository.insertForecast(
                                ForecastEntity(
                                    cityId = it.city.cityId,
                                    name = it.city.name,
                                    date = gap.dt * TIME_DIF,
                                    temp = gap.main.temp.roundToInt(),
                                    iconId = gap.weather.firstOrNull()?.id ?: 0
                                )
                            ).subscribe()
                        }
                    }
                }, {
                    Timber.e(it)
                })
    }

    override fun purgeForecast(cityId: Int): Completable {
        return repository.purgeForecast(cityId)
    }

    companion object {
        private const val DEFAULT_POSITION = 0
        private const val DEFAULT_TIME_OUT = 10L
        private const val TIME_DIF = 1000
        private const val MIDDLE_DAY_STRING = "12:00:00"
    }
}


