package ru.nightgoat.weather.presentation.city

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.domain.IInteractor
import ru.nightgoat.weather.presentation.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class CityViewModel @Inject constructor(private val interactor: IInteractor) : BaseViewModel() {

    val cityLiveData = MutableLiveData<CityEntity>()
    val forecastLiveData = MutableLiveData<MutableList<ForecastEntity>>()
    val refreshLiveData = MutableLiveData<Boolean>()

    fun loadWeather(id: Int, units: String, apiKey: String) {
        compositeDisposable.addAll(
            interactor.purgeForecast(id)
                .doOnSubscribe {
                    refreshLiveData.value = true
                }
                .andThen(interactor.updateForecast(id, units, apiKey))
                .andThen(
                    interactor.getCityFromDataBaseAndUpdateFromApi(
                        cityId = id,
                        units = units,
                        apiKey = apiKey
                    )
                )
                .doOnNext {
                    cityLiveData.postValue(it)
                }
                .flatMap {
                    interactor.getForecast(id)
                }.doOnNext {
                    forecastLiveData.postValue(it.asReversed())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refreshLiveData.value = false
                }, {
                    Timber.e(it)
                    refreshLiveData.value = false
                }),
        )
    }

    fun purgeForecast(cityId: Int) {
        interactor.purgeForecast(cityId).subscribeOn(Schedulers.io()).subscribe()
    }
}