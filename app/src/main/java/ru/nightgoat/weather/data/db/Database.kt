package ru.nightgoat.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nightgoat.weather.data.entity.CityEntity

@Database(entities = arrayOf(CityEntity::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dao() : Dao
}