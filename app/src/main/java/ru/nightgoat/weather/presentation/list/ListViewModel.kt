package ru.nightgoat.weather.presentation.list

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.nightgoat.weather.core.utils.NOT_FOUND_KEY
import ru.nightgoat.weather.core.utils.orIfNull
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.domain.IInteractor
import ru.nightgoat.weather.presentation.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class ListViewModel @Inject constructor(private val interactor: IInteractor) : BaseViewModel() {

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
                    Timber.e(it.message.orEmpty())
                })
        )
    }

    fun getCityFromApiAndPutInDB(name: String, units: String, api_key: String) {
        compositeDisposable.add(
            interactor.getCityFromApiAndPutInDB(name, units, api_key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ entity ->
                    entity?.let {
                        cityIdLiveData.value = it.cityId
                    }.orIfNull {
                        snackBarLiveData.value = NOT_FOUND_KEY
                    }
                }, {
                    Timber.e(it)
                    val isThisIsBadAnswer = it.message?.contains(BAD_ANSWER, ignoreCase = true)
                    if (isThisIsBadAnswer == true) {
                        snackBarLiveData.value = NOT_FOUND_KEY
                    } else {
                        snackBarLiveData.value = it.message
                    }
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

    fun updateAllFromApi(units: String, API_KEY: String) {
        Timber.d("update() call")
        compositeDisposable.add(
            interactor.updateAllFromApi(units, API_KEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refreshLiveData.value = false
                }, {
                    Timber.e(it)
                })
        )
    }

    fun updateAllInRepository(cities: MutableList<CityEntity>) {
        for (city in cities) {
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
        compositeDisposable.add(
            interactor.swapPositionWithFirst(city).subscribe(
                {},
                { Timber.e(it) })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        private const val BAD_ANSWER = "404"
    }
}