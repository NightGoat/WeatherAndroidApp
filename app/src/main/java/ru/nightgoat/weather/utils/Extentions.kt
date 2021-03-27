package ru.nightgoat.weather.utils

import android.content.SharedPreferences
import ru.nightgoat.weather.R

fun getUnits(sharedPreferences: SharedPreferences?): String {
    val degreeKey = sharedPreferences?.getInt(DEGREE_KEY, R.id.settings_radBtnCelsius)
    return METRIC.takeIf { degreeKey == R.id.settings_radBtnCelsius } ?: IMPERIAL
}

fun getApiKey(sharedPreferences: SharedPreferences?): String {
    return sharedPreferences?.getString(API_KEY, "0").orEmpty()
}