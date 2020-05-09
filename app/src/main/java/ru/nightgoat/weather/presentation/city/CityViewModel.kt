package ru.nightgoat.weather.presentation.city

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.presentation.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class CityViewModel @Inject constructor(private val interactor: Interactor) : BaseViewModel() {

    val cityLiveData = MutableLiveData<CityEntity>()
    val forecastLiveData = MutableLiveData<MutableList<ForecastEntity>>()
    val refreshLiveData = MutableLiveData<Boolean>()

    fun loadWeather(id: Int, units: String, API_KEY: String) {
        compositeDisposable.addAll(
            interactor.getCityFromDataBaseAndUpdateFromApi(
            id,
            units,
            API_KEY
        )
            .observeOn(AndroidSchedulers.mainThread(), true)
            .doOnSubscribe {
                refreshLiveData.value = true
            }
            .subscribe(
                {
                cityLiveData.value = it
                refreshLiveData.value = false

            }, {
                Timber.e( "city ${it.message!!}")
                refreshLiveData.value = false
            }),

            interactor.getForecast(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    forecastLiveData.value = it.asReversed()
                }, {
                    Timber.e( "forecast ${it.message!!}")
                }),

            interactor.updateForecast(id, units, API_KEY)
        )
    }

    fun purgeForecast(cityId: Int) {
        interactor.purgeForecast(cityId).subscribeOn(Schedulers.io()).subscribe()
    }
}