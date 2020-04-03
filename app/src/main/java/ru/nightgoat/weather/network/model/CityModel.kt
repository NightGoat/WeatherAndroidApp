package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
    var list: List<TimeGap>? = null,

    @SerializedName("city")
    @Expose
    var city: CityModel? = null
    )

data class Wind(
    @SerializedName("speed")
    @Expose
    var speed: Float
)

data class Weather(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("icon")
    @Expose
    var icon: String
)

data class Main(
    @SerializedName("temp")
    @Expose
    var temp: Double,

    @SerializedName("feels_like")
    @Expose
    var feelsLike: Double,

    @SerializedName("temp_min")
    @Expose
    var tempMin: Double,

    @SerializedName("temp_max")
    @Expose
    var tempMax: Double,

    @SerializedName("pressure")
    @Expose
    var pressure: Int,

    @SerializedName("humidity")
    @Expose
    var humidity: Int
)

data class Sys(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("country")
    @Expose
    var country: String,

    @SerializedName("sunrise")
    @Expose
    var sunrise: Long,

    @SerializedName("sunset")
    @Expose
    var sunset: Long
)

data class TimeGap (

    @SerializedName("dt")
    @Expose
    var dt: Int,

    @SerializedName("main")
    @Expose
    var main: Main)