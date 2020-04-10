package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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