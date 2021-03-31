package ru.nightgoat.weather.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.core.extentions.orZero
import ru.nightgoat.weather.core.utils.orIfNull
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

    private val defaultScheduler = Schedulers.io()

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
    ): Single<CityEntity?> {
        return api.getCurrentWeather(
            city,
            api_key,
            units,
            Locale.getDefault().country
        )
            .subscribeOn(defaultScheduler)
            .map { cityModel: CityModel ->
                cityModel.convertToCityEntity()
            }
            .doOnSuccess { entity ->
                entity?.let {
                    it.position = cityList.size
                    repository.insertCity(it).subscribe()
                }
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
                ).observeOn(defaultScheduler)
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
        return cityList.firstOrNull { it.position == DEFAULT_POSITION }?.let { firstCity ->
            firstCity.position = cityToSwap.position
            cityToSwap.position = DEFAULT_POSITION
            repository.updateCity(firstCity)
                .andThen(repository.updateCity(cityToSwap)).subscribeOn(defaultScheduler)
        }.orIfNull {
            cityToSwap.position = DEFAULT_POSITION
            repository.updateCity(cityToSwap).subscribeOn(defaultScheduler)
        }
    }

    override fun updateCityInDB(city: CityEntity): Completable {
        return repository.insertCity(city).subscribeOn(defaultScheduler)
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
                .doOnSuccess { entity ->
                    entity?.let {
                        repository.insertCity(it).subscribeOn(defaultScheduler).subscribe()
                    }
                }
                .doOnError {
                    Timber.e(it)
                })
            .subscribeOn(defaultScheduler)
    }

    override fun getSearchList(): Single<MutableList<String>> {
        return repository.getSearchList().subscribeOn(defaultScheduler)
    }

    override fun insertSearchEntity(city: SearchEntity): Completable {
        return repository.insertSearchEntity(city).subscribeOn(defaultScheduler)
    }

    override fun purgeSearchList(): Completable {
        return repository.purgeSearchList().subscribeOn(defaultScheduler)
    }

    override fun getForecast(cityId: Int): Flowable<MutableList<ForecastEntity>> {
        return repository.getForecast(cityId).subscribeOn(defaultScheduler)
    }

    override fun updateForecast(cityId: Int, units: String, API_KEY: String): Disposable {
        return api.getForecast(
            id = cityId,
            app_id = API_KEY,
            units = units,
            lang = Locale.getDefault().country
        )
            .subscribeOn(defaultScheduler)
            .subscribe(
                { cityModel ->
                    cityModel.list?.let { gaps ->
                        for (gap in gaps) {
                            if (gap.dtTxt?.contains(MIDDLE_DAY_STRING) == true) {
                                val cityValue = cityModel.city
                                cityValue?.cityId?.let { cityIdValue ->
                                    cityValue.name?.let { cityNameValue ->
                                        gap.main?.temp?.let { tempValue ->
                                            repository.insertForecast(
                                                ForecastEntity(
                                                    cityId = cityIdValue,
                                                    name = cityNameValue,
                                                    date = gap.dt.orZero() * TIME_DIF,
                                                    temp = tempValue.roundToInt().orZero(),
                                                    iconId = gap.weather?.firstOrNull()?.id.orZero()
                                                )
                                            ).subscribe()
                                        } ?: Timber.e("updateForecast(): temp null")
                                    } ?: Timber.e("updateForecast(): city name null")
                                } ?: Timber.e("updateForecast(): cityId null")
                            }
                        }
                    } ?: Timber.e("updateForecast(): forecast null")
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


