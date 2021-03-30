package ru.nightgoat.weather.core.utils

import android.content.SharedPreferences
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.extentions.orZero

fun SharedPreferences?.getUnits(): String {
    val degreeKey = this?.getInt(DEGREE_KEY, R.id.settings_radBtnCelsius)
    return METRIC.takeIf { degreeKey == R.id.settings_radBtnCelsius } ?: IMPERIAL
}

fun SharedPreferences?.getApiKey(): String {
    return this?.getString(API_KEY, "0").orEmpty()
}

fun SharedPreferences?.getCityId(): Int = this?.getInt(CITY_ID_KEY, 0).orZero()