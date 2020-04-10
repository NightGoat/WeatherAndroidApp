package ru.nightgoat.weather.presentation.addCity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.nightgoat.weather.data.entity.SearchEntity
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.presentation.base.BaseViewModel
import javax.inject.Inject

class AddCityViewModel @Inject constructor(private val interactor: Interactor) : BaseViewModel() {

    val searchListLiveData = MutableLiveData<MutableList<String>>()

    init {
        compositeDisposable.add(
            interactor.getSearchList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listOfSearchEntities ->
                    Log.d(TAG, listOfSearchEntities.toString())
                    searchListLiveData.value = listOfSearchEntities
                }, {
                    Log.e(TAG, it.message.toString())
                })
        )
    }

    fun addSearchEntity(name: String) {
        Log.d(TAG, "addSearchEntity")
        compositeDisposable.add(
            interactor.insertSearchEntity(SearchEntity(0, name)).subscribe()
        )
    }

    fun purgeList(){
        Log.d(TAG, "purgeList")
        compositeDisposable.add(
            interactor.purgeSearchList().subscribe()
        )
    }

    companion object {
        val TAG = AddCityViewModel::class.java.simpleName
    }
}
