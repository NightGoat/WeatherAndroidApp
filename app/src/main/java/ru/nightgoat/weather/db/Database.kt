package ru.nightgoat.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(CityEntity::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dao() : Dao
}