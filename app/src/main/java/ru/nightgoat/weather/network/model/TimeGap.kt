package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TimeGap(

    @SerializedName("dt")
    @Expose
    val dt: Long?,

    @SerializedName("main")
    @Expose
    val main: Main?,

    @SerializedName("weather")
    @Expose
    val weather: List<Weather>?,

    @SerializedName("dt_txt")
    @Expose
    val dtTxt: String?,

    val sunrise: Long,
    val sunset: Long
)