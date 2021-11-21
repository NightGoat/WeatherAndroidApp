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
            interactor.getCityFromDataBaseAndUpdateFromApi(
                cityId = id,
                units = units,
                API_KEY = apiKey
            ).observeOn(AndroidSchedulers.mainThread(), true)
                .doOnSubscribe {
                    refreshLiveData.value = true
                }
                .subscribe(
                    {
                        cityLiveData.value = it
                        refreshLiveData.value = false

                    }, {
                        Timber.e("city ${it.message}")
                        refreshLiveData.value = false
                    }),

            interactor.purgeForecast(id).observeOn(AndroidSchedulers.mainThread()).subscribe(),

            interactor.getForecast(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    forecastLiveData.value = it.asReversed()
                }, {
                    Timber.e("forecast ${it.message}")
                }),

            interactor.updateForecast(id, units, apiKey)
        )
    }

    fun purgeForecast(cityId: Int) {
        interactor.purgeForecast(cityId).subscribeOn(Schedulers.io()).subscribe()
    }
}