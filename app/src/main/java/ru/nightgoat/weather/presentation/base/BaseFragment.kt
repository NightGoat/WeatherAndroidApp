package ru.nightgoat.weather.presentation.base

import android.content.SharedPreferences
import dagger.android.support.DaggerFragment
import ru.nightgoat.weather.R
import ru.nightgoat.weather.presentation.city.CityFragment
import ru.nightgoat.weather.utils.getHour
import ru.nightgoat.weather.utils.pressureFromHPaToMmHg
import java.util.*

abstract class BaseFragment : DaggerFragment() {
    lateinit var sharedPreferences: SharedPreferences

    fun chooseCityId(): Int {
        return sharedPreferences.getInt("cityId", 0)
    }

    fun chooseUnits(): String {
        return ru.nightgoat.weather.utils.chooseUnits(sharedPreferences)
    }

    fun choosePressure(value: Int): String {
        return if (sharedPreferences.getInt("pressure", R.id.settings_radBtnMmHg)
            == R.id.settings_radBtnMmHg
        ) pressureFromHPaToMmHg(value).plus(
            getString(R.string.mmHg)
        )
        else value.toString().plus(getString(R.string.hPa))
    }

    fun chooseIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String {
        return ru.nightgoat.weather.utils.chooseIcon(id, dt, sunrise, sunset, context!!)
    }

}