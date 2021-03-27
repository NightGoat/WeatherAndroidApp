package ru.nightgoat.weather.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import ru.nightgoat.weather.R

fun chooseIcon(
    id: Int,
    dt: Long,
    sunrise: Long,
    sunset: Long,
    context: Context
): String {
    return when (id) {
        in 200..201 -> return context.getString(R.string.weather_thunder_lightRain) //Thunderstorm with light rain
        202 -> return context.getString(R.string.weather_thunder_heavyRain) // thunderstorm with heavy rain
        in 202..232 -> return context.getString(R.string.weather_thunder) //thunderstorm
        in 300..321 -> return context.getString(R.string.weather_drizzle) //Drizzle
        in 500..511 -> return context.getString(R.string.weather_rain) //rain
        in 520..531 -> return context.getString(R.string.weather_showerRain) //shower rain
        in 600..622 -> return context.getString(R.string.weather_snow) //snow
        701, 721, 741 -> return context.getString(R.string.weather_fog) //Fog, Mist, Haze
        711 -> return context.getString(R.string.weather_smoke) //Smoke
        731, 751 -> return context.getString(R.string.weather_sand) //Sand
        761 -> return context.getString(R.string.weather_dust) //Dust
        762 -> return context.getString(R.string.weather_volcano) //Volcanic ash
        771 -> return context.getString(R.string.weather_squall) //Squall
        781 -> return context.getString(R.string.weather_tornado) //Tornado
        800 -> {
            if ((sunrise == 0L) or (dt in sunrise until sunset)) context.getString(R.string.weather_sunny) //sunny
            else context.getString(R.string.weather_clear_night) //night
        }
        801 -> {
            if ((sunrise == 0L) or (dt in sunrise until sunset)) context.getString(R.string.weather_sun_clouds)
            else context.getString(R.string.weather_moon_clouds)
        }
        in 802..804 -> return context.getString(R.string.weather_clouds) //clouds
        else -> "nope"
    }
}

fun chooseUnits(sharedPreferences: SharedPreferences?): String {
    return if (sharedPreferences?.getInt("degree", R.id.settings_radBtnCelsius)
        == R.id.settings_radBtnCelsius
    ) "metric"
    else "imperial"
}

fun convertToImg(text: String, context: Context, textSize: Float): Bitmap {
    val paint = Paint()
    paint.isAntiAlias = true
    paint.isSubpixelText = true
    paint.typeface = Typeface.createFromAsset(context.assets, "fonts/weathericons.ttf")
    paint.style = Paint.Style.FILL
    paint.color = Color.WHITE
    paint.textSize = textSize
    paint.textAlign = Paint.Align.LEFT
    val baseline = -paint.ascent()
    val btmText = Bitmap.createBitmap((paint.measureText(text) + 0.5f).toInt(),
        (baseline  + paint.descent() + 0.5f).toInt(), Bitmap.Config.ARGB_8888)
    val cnvText = Canvas(btmText)
    cnvText.drawText(text, 0F, baseline, paint)
    return btmText
}

fun getApiKey(sharedPreferences: SharedPreferences?) : String {
    return sharedPreferences?.getString("api_key", "0").orEmpty()
}