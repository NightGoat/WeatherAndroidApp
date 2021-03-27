package ru.nightgoat.weather.presentation.addCity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.nightgoat.weather.data.entity.SearchEntity
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.presentation.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class AddCityViewModel @Inject constructor(private val interactor: Interactor) : BaseViewModel() {

    val searchListLiveData = MutableLiveData<MutableList<String>>()

    init {
        compositeDisposable.add(
            interactor.getSearchList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listOfSearchEntities ->
                    Timber.tag(TAG).d(listOfSearchEntities.toString())
                    searchListLiveData.value = listOfSearchEntities
                }, {
                    Timber.tag(TAG).e(it.message.toString())
                })
        )
    }

    fun addSearchEntity(name: String) {
        Timber.tag(TAG).d("addSearchEntity")
        compositeDisposable.add(
            interactor.insertSearchEntity(SearchEntity(0, name)).subscribe()
        )
    }

    fun purgeList() {
        Timber.tag(TAG).d("purgeList")
        compositeDisposable.add(
            interactor.purgeSearchList().subscribe()
        )
    }

    companion object {
        val TAG = AddCityViewModel::class.java.simpleName
    }
}
