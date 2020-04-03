package ru.nightgoat.weather.presentation.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.network.API
import ru.nightgoat.weather.presentation.BaseViewModel
import ru.nightgoat.weather.utils.API_ID
import ru.nightgoat.weather.utils.getCurrentDate
import java.util.*
import javax.inject.Inject

class ListViewModel : BaseViewModel() {

    @Inject
    lateinit var api: API

    var units = "metric"
    val cityListLiveData = MutableLiveData<MutableList<CityEntity>>()
    val snackBarLiveData = MutableLiveData<String>()
    var cityList: MutableList<CityEntity> = mutableListOf()
    val disposable: CompositeDisposable = CompositeDisposable()

    init {
        update()
    }

    fun addCity(name: String) {
        disposable.add(
            api.getCurrentWeather(name, API_ID, units, Locale.getDefault().country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cityList.add(
                        CityEntity(
                            it.id,
                            getCurrentDate(),
                            it.name,
                            it.main.temp.toInt(),
                            it.main.humidity,
                            it.main.pressure,
                            it.wind.speed.toInt(),
                            it.weather[0].description,
                            it.weather[0].id,
                            it.sys.sunrise,
                            it.sys.sunset
                        )
                    )
                    cityListLiveData.value = cityList
                }, { throwable ->
                    snackBarLiveData.value = "city not found"
                    Log.e("ListViewModel addCity", throwable.message.toString())
                })
        )
    }

    fun update() {
        if (cityList.isNotEmpty()) {
            val newList = mutableListOf<CityEntity>()
            for (cityEntity in cityList) {
                disposable.add(api.getCurrentWeather(cityEntity.name, API_ID, units, Locale.getDefault().country)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        newList.add(
                            CityEntity(
                                it.id,
                                getCurrentDate(),
                                it.name,
                                it.main.temp.toInt(),
                                it.main.humidity,
                                it.main.pressure,
                                it.wind.speed.toInt(),
                                it.weather[0].description,
                                it.weather[0].id,
                                it.sys.sunrise,
                                it.sys.sunset
                            )
                        )
                    }, {
                        Log.e("listviewmodel update: ", it.message.toString())
                    }))
            }
            cityList.clear()
            cityList.addAll(newList)
            cityListLiveData.value = cityList
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}