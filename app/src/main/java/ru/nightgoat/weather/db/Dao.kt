package ru.nightgoat.weather.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface Dao {

    @Insert
    fun insert(city: CityEntity)

}