package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.nightgoat.weather.data.entity.CityEntity

data class CityModel(

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>,

    @SerializedName("main")
    @Expose
    var main: Main,

    @SerializedName("wind")
    @Expose
    var wind: Wind,

    @SerializedName("dt")
    @Expose
    var dt: Long,

    @SerializedName("sys")
    @Expose
    var sys: Sys,

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("list")
    @Expose
    var list: List<TimeGap>,

    @SerializedName("city")
    @Expose
    var city: City,

    var position: Int
) {
    fun convertToCityEntity(): CityEntity {
        return CityEntity(
            cityId = id,
            position = position,
            date = dt*1000,
            name = name,
            country = sys.country,
            temp = main.temp.toInt(),
            feelsTemp = main.feelsLike.toInt(),
            maxTemp = main.tempMax.toInt(),
            minTemp = main.tempMin.toInt(),
            humidity = main.humidity,
            pressure = main.pressure,
            wind = wind.speed.toInt(),
            description = weather[0].description,
            iconId = weather[0].id,
            sunrise = sys.sunrise,
            sunset = sys.sunset)
    }
}





