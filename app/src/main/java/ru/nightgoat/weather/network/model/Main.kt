package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp")
    @Expose
    val temp: Double?,

    @SerializedName("feels_like")
    @Expose
    val feelsLike: Double?,

    @SerializedName("temp_min")
    @Expose
    val tempMin: Double?,

    @SerializedName("temp_max")
    @Expose
    val tempMax: Double?,

    @SerializedName("pressure")
    @Expose
    val pressure: Int?,

    @SerializedName("humidity")
    @Expose
    val humidity: Int?
)