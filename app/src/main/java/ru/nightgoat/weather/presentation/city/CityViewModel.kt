package ru.nightgoat.weather.presentation.city

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.network.model.CityModel
import ru.nightgoat.weather.network.model.TimeGap
import ru.nightgoat.weather.presentation.base.BaseViewModel
import ru.nightgoat.weather.utils.getHour
import javax.inject.Inject

class CityViewModel @Inject constructor(private val interactor: Interactor) : BaseViewModel() {

    val cityLiveData = MutableLiveData<CityEntity>()
    val forecastLiveData = MutableLiveData<MutableList<TimeGap>>()
    val refreshLiveData = MutableLiveData<Boolean>()

    fun loadWeather(id: Int, units: String) {
        compositeDisposable.add(
            interactor.getCityFromDataBaseAndUpdateFromApi(id, units)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    refreshLiveData.value = true
                }
                .subscribe({
                    Log.d(TAG, "subscribe: $it")
                    cityLiveData.value = it
                    refreshLiveData.value = false
                }, {
                    Log.e(TAG, "city ${it.message!!}")
                    refreshLiveData.value = false
                })
        )

        compositeDisposable.add(
            interactor.getForecast(id, units)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val forecastList = mutableListOf<TimeGap>()
                    val gapList = it.list
                    Log.d(TAG, it.list.size.toString())
                    for (gap in gapList) {
                        if (getHour(gap.dt*1000) == 12) {
                            gap.sunrise = it.city.sunrise
                            gap.sunset = it.city.sunset
                            forecastList.add(gap)
                        }
                    }
                    forecastLiveData.value = forecastList
                }, {
                    Log.e(TAG, "forecast ${it.message!!}")
                })
        )
    }

    companion object {
        @JvmStatic
        val TAG = CityViewModel::class.java.simpleName
    }
}