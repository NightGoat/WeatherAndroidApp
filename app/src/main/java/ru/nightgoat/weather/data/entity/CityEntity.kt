package ru.nightgoat.weather.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityEntity(
    @PrimaryKey
    val cityId: Int,
    val position: Int,
    val date: Long,
    val name: String,
    val country: String?,
    val temp: Int,
    val feelsTemp: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val humidity: Int,
    val pressure: Int,
    val wind: Int,
    val description: String,
    val iconId: Int,
    val sunrise: Long,
    val sunset: Long
)