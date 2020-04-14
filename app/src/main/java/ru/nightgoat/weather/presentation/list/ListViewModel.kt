package ru.nightgoat.weather.presentation.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.presentation.base.BaseViewModel
import ru.nightgoat.weather.utils.getApiKey
import javax.inject.Inject

class ListViewModel @Inject constructor(private val interactor: Interactor) : BaseViewModel() {

    val cityListLiveData = MutableLiveData<MutableList<CityEntity>>()
    val snackBarLiveData = MutableLiveData<String>()
    val refreshLiveData = MutableLiveData<Boolean>()
    val cityIdLiveData = MutableLiveData<Int>()

    init {
        compositeDisposable.add(
            interactor
                .getAllCities()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listOfCities ->
                    cityListLiveData.value = listOfCities
                }, {
                    Log.e(TAG, it.message!!)
                })
        )
    }

    fun getCityFromApiAndPutInDB(name: String, units: String, api_key: String) {
        compositeDisposable.add(
            interactor.getCityFromApiAndPutInDB(name, units, api_key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cityIdLiveData.value = it.cityId
                }, {
                    if (it.message!!.contains("404", ignoreCase = true))
                        snackBarLiveData.value = "nf"
                    else snackBarLiveData.value = it.message
                })
        )
    }

    fun deleteCity(cityEntity: CityEntity) {
        compositeDisposable
            .add(
                interactor.deleteCity(cityEntity)
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            )
    }

    fun updateAllFromApi(units: String, API_KEY : String) {
        Log.d(TAG, "update() call")
        compositeDisposable.add(
            interactor.updateAllFromApi(units, API_KEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refreshLiveData.value = false
                }, {
                    Log.e(TAG, it.message.toString())
                })
        )
    }

    fun updateAllInRepository(list: MutableList<CityEntity>) {
        for (city in list) {
            compositeDisposable.add(
                interactor
                    .updateCityInDB(city)
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            )
        }
    }

    fun updateCityInDB(city: CityEntity) {
        compositeDisposable.add(
            interactor.updateCityInDB(city).subscribe()
        )
    }

    fun swapPositionWithFirst(city: CityEntity) {
        compositeDisposable.add(interactor.swapPositionWithFirst(city).subscribe({

        }, {
            Log.e(TAG, it.message.toString())
        }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        @JvmStatic
        val TAG = ListViewModel::class.java.simpleName
    }
}