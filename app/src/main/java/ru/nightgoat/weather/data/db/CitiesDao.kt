package ru.nightgoat.weather.data.db

import androidx.room.*
import androidx.room.Dao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.nightgoat.weather.data.entity.CityEntity

@Dao
interface CitiesDao {

    @Query("SELECT * FROM CityEntity ORDER BY position")
    fun getAllCities(): Flowable<MutableList<CityEntity>>

    @Query("SELECT * FROM CityEntity")
    fun getAllCities2(): Single<MutableList<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: CityEntity): Completable

    @Delete
    fun deleteCity(city: CityEntity): Completable

    @Update
    fun updateCity(city: CityEntity): Completable

    @Query("SELECT * FROM CityEntity WHERE name = :name")
    fun getCity(name: String) : Maybe<CityEntity>

    @Query("SELECT * FROM CityEntity WHERE cityId = :cityId")
    fun getCityById(cityId: Int) : Maybe<CityEntity>
}