package ru.nightgoat.weather.data.db

import androidx.room.Dao
import androidx.room.Insert
import ru.nightgoat.weather.data.entity.CityEntity

@Dao
interface Dao {

    @Insert
    fun insert(city: CityEntity)

}