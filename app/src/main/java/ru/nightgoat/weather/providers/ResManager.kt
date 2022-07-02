package ru.nightgoat.weather.providers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.extentions.createTypeFace
import ru.nightgoat.weather.core.utils.*

class ResManager(val context: Context) : IResManager {

    override fun getWeatherIcon(
        id: Int,
        dt: Long,
        sunrise: Long,
        sunset: Long,
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
            CLEAR_SKY -> getSkyIcon(sunrise, dt, sunset) //night
            CLOUDY_SKY -> getCloudIcon(sunrise, dt, sunset)
            in CLOUDS_INTERVAL -> return context.getString(R.string.weather_clouds) //clouds
            else -> ""
        }
    }

    private fun getCloudIcon(
        sunrise: Long,
        dt: Long,
        sunset: Long
    ) = if ((sunrise == 0L) or (dt in sunrise until sunset)) {
        context.getString(R.string.weather_sun_clouds)
    } else {
        context.getString(R.string.weather_moon_clouds)
    }

    private fun getSkyIcon(sunrise: Long, dt: Long, sunset: Long) =
        if ((sunrise == 0L) or (dt in sunrise until sunset)) {
            context.getString(R.string.weather_sunny) //sunny
        } else {
            context.getString(R.string.weather_clear_night)
        }

    override fun convertToImg(text: String, textSize: Float): Bitmap {
        val paint = Paint().apply {
            isAntiAlias = true
            isSubpixelText = true
            typeface = context.createTypeFace()
            style = Paint.Style.FILL
            color = Color.WHITE
            textAlign = Paint.Align.LEFT
        }
        paint.textSize = textSize
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

interface IResManager {
    fun getWeatherIcon(
        id: Int,
        dt: Long,
        sunrise: Long,
        sunset: Long,
    ): String

    fun convertToImg(text: String, textSize: Float): Bitmap
}