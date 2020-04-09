package ru.nightgoat.weather.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

const val TAG = "ViewModelFactory"

@Singleton
class ViewModelFactory
@Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator: Provider<out ViewModel> =
            creators[modelClass]
                ?: creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }!!.value
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            Log.e(TAG, "Error is here : " + e.message, e)
            throw RuntimeException(e)
        }
    }
}