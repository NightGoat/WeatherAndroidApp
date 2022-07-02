package ru.nightgoat.weather.domain

import io.github.nightgoat.kexcore.orIfNull
import io.github.nightgoat.kexcore.orZero
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity
import ru.nightgoat.weather.network.OpenWeatherAPI
import ru.nightgoat.weather.network.model.CityModel
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class WeatherInteractor(private val repository: DBRepository, private val api: OpenWeatherAPI) :
    IInteractor {

    private var cityList: MutableList<CityEntity> = mutableListOf()

    private val defaultScheduler = Schedulers.io()

    override fun getAllCities(): Flowable<MutableList<CityEntity>> {
        return repository.getAllCities().subscribeOn(Schedulers.io()).map {
            it.onEach { entity ->
                entity.position = it.indexOf(entity)
            }
        }.doOnNext { cityList = it }
    }

    override fun deleteCity(cityEntity: CityEntity): Completable {
        return repository.deleteCity(cityEntity)
            .andThen(repository.deleteAllForecastForCity(cityEntity.cityId))
    }

    override fun getCityFromApiAndPutInDB(
        city: String,
        units: String,
        apiKey: String
    ): Single<CityEntity> {
        return api.getCurrentWeather(
            city = city,
            appId = apiKey,
            units = units,
            lang = Locale.getDefault().country
        ).subscribeOn(defaultScheduler)
            .map { cityModel: CityModel ->
                cityModel.convertToCityEntity()
            }
            .doOnSuccess { entity ->
                entity.position = cityList.size
                repository.insertCity(entity).subscribe()
            }
    }

    override fun updateAllFromApi(units: String, apiKey: String): Completable {
        return repository.getAllCitiesSingle()
            .observeOn(defaultScheduler)
            .flattenAsObservable { Iterable { it.iterator() } }
            .flatMapSingle { cityEntity ->
                api.getCurrentWeatherById(
                    id = cityEntity.cityId,
                    appId = apiKey,
                    units = units,
                    lang = Locale.getDefault().country
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
        return cityList.firstOrNull { it.position == FIRST_POSITION }?.let { firstCity ->
            firstCity.position = cityToSwap.position
            cityToSwap.position = FIRST_POSITION
            repository.updateCity(firstCity)
                .andThen(repository.updateCity(cityToSwap)).subscribeOn(defaultScheduler)
        }.orIfNull {
            cityToSwap.position = FIRST_POSITION
            repository.updateCity(cityToSwap).subscribeOn(defaultScheduler)
        }
    }

    override fun updateCityInDB(city: CityEntity): Completable {
        return repository.insertCity(city).subscribeOn(defaultScheduler)
    }

    override fun getCityFromDataBaseAndUpdateFromApi(
        cityId: Int,
        units: String,
        apiKey: String
    ): Flowable<CityEntity> {
        return repository.getCityById(cityId)
            .concatWith(api.getCurrentWeatherById(
                id = cityId,
                appId = apiKey,
                units = units,
                lang = Locale.getDefault().country
            ).map {
                it.convertToCityEntity()
            }.toMaybe()
                .timeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .doOnSuccess { entity ->
                    repository.insertCity(entity).subscribeOn(defaultScheduler).subscribe()
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

    override fun updateForecast(cityId: Int, units: String, apiKey: String): Completable {
        return api.getForecast(
            id = cityId,
            appId = apiKey,
            units = units,
            lang = Locale.getDefault().country
        ).subscribeOn(defaultScheduler)
            .flatMapMaybe { cityModel ->
                val mappedGaps = cityModel.list.orEmpty().mapNotNull { gap ->
                    if (gap.dtTxt?.contains(MIDDLE_DAY_STRING) == true) {
                        val cityValue = cityModel.city
                        cityValue?.cityId?.let { cityIdValue ->
                            cityValue.name?.let { cityNameValue ->
                                gap.main?.temp?.let { tempValue ->
                                    ForecastEntity(
                                        cityId = cityIdValue,
                                        name = cityNameValue,
                                        date = gap.dt.orZero() * TIME_DIF,
                                        temp = tempValue.roundToInt().orZero(),
                                        iconId = gap.weather?.firstOrNull()?.id.orZero()
                                    )
                                }
                            }
                        }
                    } else {
                        null
                    }
                }
                Maybe.just(mappedGaps)
            }.flatMapCompletable {
                if (it.isNotEmpty()) {
                    repository.insertForecast(it)
                } else {
                    Completable.error {
                        IllegalStateException("list is empty")
                    }
                }
            }
    }

    override fun purgeForecast(cityId: Int): Completable {
        return repository.purgeForecast(cityId).subscribeOn(defaultScheduler)
    }

    companion object {
        private const val FIRST_POSITION = 0
        private const val DEFAULT_TIME_OUT = 10L
        private const val TIME_DIF = 1000
        private const val MIDDLE_DAY_STRING = "12:00:00"
    }
}


