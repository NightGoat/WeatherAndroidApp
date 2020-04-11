package ru.nightgoat.weather.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastEntity(
    @PrimaryKey
    var cityId: Int,
    var date: Long,
    var sunrise: Long,
    var sunset: Long,
    var name: String,
    var firstTemp: Int,
    var firstWeatherIconId: Int,
    var secondTemp: Int,
    var secondWeatherIconId: Int,
    var thirdTemp: Int,
    var thirdWeatherIconId: Int,
    var forthTemp: Int,
    var forthWeatherIconId: Int,
    var fifthTemp: Int,
    var fifthWeatherIconId: Int
)