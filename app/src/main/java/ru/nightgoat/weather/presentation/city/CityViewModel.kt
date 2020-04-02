package ru.nightgoat.weather.presentation.city

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.model.CityModel
import ru.nightgoat.weather.network.API
import ru.nightgoat.weather.presentation.BaseViewModel
import ru.nightgoat.weather.utils.API_ID
import java.util.*
import javax.inject.Inject

class CityViewModel: BaseViewModel() {
    @Inject
    lateinit var api: API

    val city: MutableLiveData<CityModel> = MutableLiveData()
    var cityName = "Kazan"
    var units = "metric"

    private lateinit var subscription: Disposable

    init {
        loadWeather()
    }

    private fun loadWeather(){
        subscription = api.getCurrentWeather(cityName, API_ID, units, Locale.getDefault().country)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { city.value = it }
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}