package ru.nightgoat.weather.presentation.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.extentions.getApiKey
import ru.nightgoat.weather.core.extentions.getCityId
import ru.nightgoat.weather.core.extentions.getUnits
import ru.nightgoat.weather.core.utils.PRESSURE_KEY
import ru.nightgoat.weather.core.utils.SETTINGS_KEY
import ru.nightgoat.weather.core.utils.pressureFromHPaToMmHg
import ru.nightgoat.weather.providers.IResManager
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var resManager: IResManager

    val sharedPreferences: SharedPreferences? by lazy {
        context?.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
    }

    private val navController by lazy {
        findNavController()
    }

    val apiKey: String
        get() = sharedPreferences.getApiKey()

    val cityId: Int
        get() = sharedPreferences.getCityId()

    val units: String
        get() = sharedPreferences.getUnits()


    fun choosePressure(value: Int): String {
        val pressure = sharedPreferences?.getInt(PRESSURE_KEY, R.id.settings_radBtnMmHg)
        return if (pressure == R.id.settings_radBtnMmHg) {
            val mmHg = getString(R.string.mmHg)
            val newValue = pressureFromHPaToMmHg(value)
            "$newValue $mmHg"
        } else {
            val hPa = getString(R.string.hPa)
            "$value $hPa"
        }
    }

    fun chooseIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String {
        return resManager.getWeatherIcon(id, dt, sunrise, sunset)
    }

    fun navigateTo(action: Int) {
        navController.navigate(action)
    }

    fun navigateTo(action: Int, bundle: Bundle) {
        navController.navigate(action, bundle)
    }

    fun showSnackBar(text: String, length: Int = Snackbar.LENGTH_SHORT, view: View) {
        Snackbar.make(view, text, length).show()
    }
}