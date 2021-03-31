package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("sunrise")
    @Expose
    val sunrise: Long?,

    @SerializedName("sunset")
    @Expose
    val sunset: Long?,

    @SerializedName("id")
    @Expose
    val cityId: Int?,

    @SerializedName("name")
    @Expose
    val name: String?

)