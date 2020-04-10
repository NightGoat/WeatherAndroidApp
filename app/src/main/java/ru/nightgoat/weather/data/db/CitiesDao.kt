package ru.nightgoat.weather.data.db

import androidx.room.*
import androidx.room.Dao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.SearchEntity

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

    @Insert
    fun insertCitySearch(city: SearchEntity): Completable

    @Query("SELECT name FROM SearchEntity ORDER BY id DESC")
    fun getSearchList(): Single<MutableList<String>>

    @Query("DELETE FROM SearchEntity where id NOT IN (SELECT id from SearchEntity ORDER BY id DESC LIMIT 10)")
    fun purgeCitySearch() : Completable
}