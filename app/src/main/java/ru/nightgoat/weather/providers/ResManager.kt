package ru.nightgoat.weather.providers

import android.content.Context
import android.graphics.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.utils.*

class ResManager(val context: Context): IResManager {

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

    override fun convertToImg(text: String, textSize: Float): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isSubpixelText = true
        paint.typeface = Typeface.createFromAsset(context.assets, FONTS_PATH)
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent()
        val paintDif = 0.5f
        val btmText = Bitmap.createBitmap(
            (paint.measureText(text) + paintDif).toInt(),
            (baseline + paint.descent() + paintDif).toInt(), Bitmap.Config.ARGB_8888
        )
        val cnvText = Canvas(btmText)
        cnvText.drawText(text, 0F, baseline, paint)
        return btmText
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