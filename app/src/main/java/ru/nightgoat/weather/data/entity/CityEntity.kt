package ru.nightgoat.weather.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityEntity(
    @PrimaryKey
    var cityId: Int,
    var position: Int,
    var date: Long,
    var name: String,
    var temp: Int,
    var feelsTemp: Int,
    var maxTemp: Int,
    var minTemp: Int,
    var humidity: Int,
    var pressure: Int,
    var wind: Int,
    var description: String,
    var iconId: Int,
    var sunrise: Long,
    var sunset: Long
)