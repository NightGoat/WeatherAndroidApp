package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("id")
    @Expose
    val id: Int?,

    @SerializedName("description")
    @Expose
    val description: String?,

    @SerializedName("icon")
    @Expose
    val icon: String?
)