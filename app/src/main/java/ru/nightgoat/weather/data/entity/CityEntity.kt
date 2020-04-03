package ru.nightgoat.weather.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class CityEntity(
    @PrimaryKey
    var id: Int,
    var date: String,
    var name: String,
    var temp: Int,
    var humidity: Int,
    var pressure: Int,
    var wind: Int,
    var description: String,
    var iconId: Int,
    var sunrise: Long,
    var sunset: Long
)