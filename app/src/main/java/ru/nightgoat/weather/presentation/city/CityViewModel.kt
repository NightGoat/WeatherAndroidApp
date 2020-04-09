package ru.nightgoat.weather.presentation.city

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.presentation.BaseViewModel
import javax.inject.Inject

class CityViewModel @Inject constructor(private val interactor: Interactor) : BaseViewModel() {

    val cityLiveData: MutableLiveData<CityEntity> = MutableLiveData()
    val refreshLiveData = MutableLiveData<Boolean>()

    fun loadWeather(id: Int, units: String) {
        compositeDisposable.add(
            interactor.getCityFromDataBaseAndUpdateFromApi(id, units)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    refreshLiveData.value = true
                }
                .subscribe({
                    cityLiveData.value = it
                    refreshLiveData.value = false
                }, {
                    Log.e(TAG, it.message!!)
                    refreshLiveData.value = false
                })
        )
    }

    companion object {
        @JvmStatic
        val TAG = CityFragment::class.java.simpleName
    }
}