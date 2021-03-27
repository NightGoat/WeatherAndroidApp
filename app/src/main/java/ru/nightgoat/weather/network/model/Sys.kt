package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("country")
    @Expose
    val country: String,

    @SerializedName("sunrise")
    @Expose
    val sunrise: Long,

    @SerializedName("sunset")
    @Expose
    val sunset: Long
)