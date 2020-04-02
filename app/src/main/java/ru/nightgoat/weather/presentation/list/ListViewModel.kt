package ru.nightgoat.weather.presentation.list

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.model.CityModel
import ru.nightgoat.weather.network.API
import ru.nightgoat.weather.presentation.BaseViewModel
import ru.nightgoat.weather.utils.API_ID
import java.util.*
import javax.inject.Inject

class ListViewModel : BaseViewModel() {

    @Inject
    lateinit var api: API

    var units = "metric"

    val cityListLiveData = MutableLiveData<MutableList<String>>()
    val snackBarLiveData = MutableLiveData<String>()
    var cityList: MutableList<String> = mutableListOf()

    fun addCity(name: String) {
        api.getCurrentWeather(name, API_ID, units, Locale.getDefault().country)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                cityList.add(it.name.toString())
                cityListLiveData.value = cityList
            }
            .doOnError { snackBarLiveData.value = "city not found" }
            .subscribe()
    }
}