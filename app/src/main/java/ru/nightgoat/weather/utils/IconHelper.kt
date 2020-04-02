package ru.nightgoat.weather.utils

import java.util.*

const val degreeIcon = "&#xf042;"
const val celsiusIcon = "&#xf03c;"
const val fahrenheitIcon = "&#xf045;"
const val thermometerIcon = "&#xf055;"
const val barometerIcon = "&#xf079;"
const val humidityIcon = "&#xf07a;"

fun weatherIcon(id: Int, sunrise: Long, sunset: Long): String {
    val currentDate = Date().time
    return when(id) {
        in 200..201 -> return "&#xf01d;"
        202 -> return "&#xf01e;"
        in 202..232 -> return "&#xf016;"
        in 300..321 -> return "&#xf01c;"
        in 500..519 -> return "&#xf019;"
        in 520..531 -> return "&#xf01a;"
        in 600..622 -> return "&#xf01b;"
        701, 721, 741 -> return "&#xf014;" //Fog, Mist, Haze
        711 -> return "&#xf062;" //Smoke
        731, 751 -> return "&#xf082;" //Sand
        761 -> return "&#xf063;" //Dust
        762 -> return "&#xf0c8;" //Volcanic ash
        771 -> return "&#xf050;" //Squall
        781 -> return "&#xf056;" //Tornado
        800 -> {
            if (currentDate in sunrise until sunset) "&#xf00d"
            else "&#xf02e;"
        }
        else -> return "&#xf013;"
    }
}