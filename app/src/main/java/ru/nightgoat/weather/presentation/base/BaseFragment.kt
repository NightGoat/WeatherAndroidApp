package ru.nightgoat.weather.presentation.base

import android.content.SharedPreferences
import dagger.android.support.DaggerFragment
import ru.nightgoat.weather.R
import ru.nightgoat.weather.presentation.city.CityFragment
import ru.nightgoat.weather.utils.pressureFromHPaToMmHg
import java.util.*

abstract class BaseFragment: DaggerFragment() {
    lateinit var sharedPreferences: SharedPreferences

    fun chooseCityId(): Int {
        return sharedPreferences.getInt("cityId", 551487)
    }

    fun chooseUnits(): String {
        return if (sharedPreferences.getInt("degree", R.id.settings_radBtnCelsius) == R.id.settings_radBtnCelsius) "metric"
        else "imperial"
    }

    fun choosePressure(value: Int): String {
        return if (sharedPreferences.getInt("pressure", R.id.settings_radBtnMmHg)
            == R.id.settings_radBtnMmHg) pressureFromHPaToMmHg(value).plus(getString(R.string.mmHg)
        )
        else value.toString().plus(getString(R.string.hPa))
    }

    fun chooseIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String {
        return when(id) {
            in 200..201 -> return getString(R.string.weather_thunder_lightRain) //Thunderstorm with light rain
            202 -> return getString(R.string.weather_thunder_heavyRain) // thunderstorm with heavy rain
            in 202..232 -> return getString(R.string.weather_thunder) //thunderstorm
            in 300..321 -> return getString(R.string.weather_drizzle) //Drizzle
            in 500..511 -> return getString(R.string.weather_rain) //rain
            in 520..531 -> return getString(R.string.weather_showerRain) //shower rain
            in 600..622 -> return getString(R.string.weather_snow) //snow
            701, 721, 741 -> return getString(R.string.weather_fog) //Fog, Mist, Haze
            711 -> return getString(R.string.weather_smoke) //Smoke
            731, 751 -> return getString(R.string.weather_sand) //Sand
            761 -> return getString(R.string.weather_dust) //Dust
            762 -> return getString(R.string.weather_volcano) //Volcanic ash
            771 -> return getString(R.string.weather_squall) //Squall
            781 -> return getString(R.string.weather_tornado) //Tornado
            800 -> {
                if (dt in sunrise until sunset) getString(R.string.weather_sunny) //sunny
                else getString(R.string.weather_clear_night) //night
            }
            else -> return getString(R.string.weather_clouds) //clouds
        }
    }
}