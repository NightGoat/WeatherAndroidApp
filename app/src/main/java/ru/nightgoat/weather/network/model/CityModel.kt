package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.nightgoat.weather.data.entity.CityEntity

data class CityModel(

    @SerializedName("weather")
    @Expose
    val weather: List<Weather>,

    @SerializedName("main")
    @Expose
    val main: Main,

    @SerializedName("wind")
    @Expose
    val wind: Wind,

    @SerializedName("dt")
    @Expose
    val dt: Long,

    @SerializedName("sys")
    @Expose
    val sys: Sys,

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("list")
    @Expose
    val list: List<TimeGap>,

    @SerializedName("city")
    @Expose
    val city: City,

    var position: Int
) {
    fun convertToCityEntity(): CityEntity {
        return CityEntity(
            cityId = id,
            position = position,
            date = dt * DATE_DIV,
            name = name,
            country = sys.country,
            temp = main.temp.toInt(),
            feelsTemp = main.feelsLike.toInt(),
            maxTemp = main.tempMax.toInt(),
            minTemp = main.tempMin.toInt(),
            humidity = main.humidity,
            pressure = main.pressure,
            wind = wind.speed.toInt(),
            description = weather.firstOrNull()?.description.orEmpty(),
            iconId = weather.firstOrNull()?.id ?: 0,
            sunrise = sys.sunrise * DATE_DIV,
            sunset = sys.sunset * DATE_DIV
        )
    }

    companion object {
        private const val DATE_DIV = 1000
    }
}





