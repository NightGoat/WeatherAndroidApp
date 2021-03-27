package ru.nightgoat.weather.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["cityId", "date"])
data class ForecastEntity(
    val cityId: Int,
    val name: String,
    val date: Long,
    val temp: Int,
    val iconId: Int
)