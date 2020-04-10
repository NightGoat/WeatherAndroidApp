package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("sunrise")
    @Expose
    var sunrise: Long,

    @SerializedName("sunset")
    @Expose
    var sunset: Long
)