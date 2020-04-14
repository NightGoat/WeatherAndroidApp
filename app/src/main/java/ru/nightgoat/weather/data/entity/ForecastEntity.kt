package ru.nightgoat.weather.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["cityId", "date"])
data class ForecastEntity(
    var cityId: Int,
    var name: String,
    var date: Long,
    var temp: Int,
    var iconId: Int,
    var sunrise: Long,
    var sunset: Long
)