package ru.nightgoat.weather.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)