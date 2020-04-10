package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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