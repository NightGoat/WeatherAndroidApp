package ru.nightgoat.weather.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityEntity (
    @field:PrimaryKey
    val id: Int,
    val date: String,
    val name: String,
    val temp: Int,
    val humidity: Int,
    val pressure: Int,
    val wind: Int
)