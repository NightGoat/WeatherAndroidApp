package ru.nightgoat.weather.presentation.base

import android.content.Context
import android.content.SharedPreferences
import dagger.android.support.DaggerFragment
import ru.nightgoat.weather.R
import ru.nightgoat.weather.utils.pressureFromHPaToMmHg
import timber.log.Timber

abstract class BaseFragment : DaggerFragment() {

    val sharedPreferences: SharedPreferences? by lazy {
        context?.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
    }

    fun chooseCityId(): Int {
        return sharedPreferences?.getInt(CITY_ID_KEY, 0) ?: 0
    }

    fun chooseUnits(): String {
        return ru.nightgoat.weather.utils.chooseUnits(sharedPreferences)
    }

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
        return ru.nightgoat.weather.utils.chooseIcon(id, dt, sunrise, sunset, requireContext())
    }

    companion object {
        private const val PRESSURE_KEY = "pressure"
        private const val CITY_ID_KEY = "cityId"
        private const val SETTINGS_KEY = "settings"
    }
}