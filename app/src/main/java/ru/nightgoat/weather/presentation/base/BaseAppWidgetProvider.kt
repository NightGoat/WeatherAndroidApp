package ru.nightgoat.weather.presentation.base

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import io.github.nightgoat.kexcore.unsafeLazy
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.extentions.createTypeFace
import ru.nightgoat.weather.core.extentions.getApiKey
import ru.nightgoat.weather.core.extentions.getCityId
import ru.nightgoat.weather.core.extentions.getUnits
import ru.nightgoat.weather.core.utils.*

abstract class BaseAppWidgetProvider: AppWidgetProvider() {

    var sharedPreferences: SharedPreferences? = null

    val apiKey by unsafeLazy {
        sharedPreferences.getApiKey()
    }

    val units by unsafeLazy {
        sharedPreferences.getUnits()
    }

    val cityId by unsafeLazy {
        sharedPreferences.getCityId()
    }

    fun Context.setSharedPreferences() {
        sharedPreferences = getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE)
    }

    fun getWeatherIcon(
        id: Int,
        dt: Long,
        sunrise: Long,
        sunset: Long,
        context: Context
    ): String {
        return when (id) {
            in THUNDERSTORM_WITH_LIGHT_RAIN_INTERVAL -> return context.getString(R.string.weather_thunder_lightRain) //Thunderstorm with light rain
            THUNDERSTORM_WITH_RAIN -> return context.getString(R.string.weather_thunder_heavyRain) // thunderstorm with heavy rain
            in THUNDERSTORM_INTERVAL -> return context.getString(R.string.weather_thunder) //thunderstorm
            in DRIZZLE_INTERVAL -> return context.getString(R.string.weather_drizzle) //Drizzle
            in RAIN_INTERVAL -> return context.getString(R.string.weather_rain) //rain
            in SHOWER_RAIN_INTERVAL -> return context.getString(R.string.weather_showerRain) //shower rain
            in SNOW_INTERVAL -> return context.getString(R.string.weather_snow) //snow
            FOG, MIST, HAZE -> return context.getString(R.string.weather_fog) //Fog, Mist, Haze
            SMOKE -> return context.getString(R.string.weather_smoke) //Smoke
            SAND_1, SAND_2 -> return context.getString(R.string.weather_sand) //Sand
            DUST -> return context.getString(R.string.weather_dust) //Dust
            VOLCANIC_ASH -> return context.getString(R.string.weather_volcano) //Volcanic ash
            SQUALL -> return context.getString(R.string.weather_squall) //Squall
            TORNADO -> return context.getString(R.string.weather_tornado) //Tornado
            CLEAR_SKY -> {
                if ((sunrise == 0L) or (dt in sunrise until sunset)) context.getString(R.string.weather_sunny) //sunny
                else context.getString(R.string.weather_clear_night) //night
            }
            CLOUDY_SKY -> {
                if ((sunrise == 0L) or (dt in sunrise until sunset)) context.getString(R.string.weather_sun_clouds)
                else context.getString(R.string.weather_moon_clouds)
            }
            in CLOUDS_INTERVAL -> return context.getString(R.string.weather_clouds) //clouds
            else -> ""
        }
    }

    fun convertToImg(text: String, textSize: Float, context: Context): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isSubpixelText = true
        paint.typeface = context.createTypeFace()
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent()
        val btmText = Bitmap.createBitmap(
            (paint.measureText(text) + PAINT_DIF).toInt(),
            (baseline + paint.descent() + PAINT_DIF).toInt(), Bitmap.Config.ARGB_8888
        )
        val cnvText = Canvas(btmText)
        cnvText.drawText(text, 0F, baseline, paint)
        return btmText
    }

    companion object {
        const val PAINT_DIF = 0.5f
    }
}