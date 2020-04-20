package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TimeGap(

    @SerializedName("dt")
    @Expose
    var dt: Long,

    @SerializedName("main")
    @Expose
    var main: Main,

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>,

    @SerializedName("dt_txt")
    @Expose
    var dtTxt: String,

    var sunrise: Long,
    var sunset: Long
)