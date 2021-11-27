package ru.nightgoat.weather.data.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity

@Dao
interface CitiesDao {

    /////////////////////////////////////////////////////////////////////////////////////////////
    //City
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

    /////////////////////////////////////////////////////////////////////////////////////////////
    //Forecast

    @Query("SELECT * FROM ForecastEntity WHERE cityId = :cityId ORDER BY date DESC LIMIT 5")
    fun getForecast(cityId: Int) : Flowable<MutableList<ForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecast: ForecastEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecast: List<ForecastEntity>): Completable

    @Query("DELETE FROM ForecastEntity WHERE cityId = :cityId")
    fun deleteForecast(cityId: Int): Completable

    @Query("DELETE FROM ForecastEntity where date not in (SELECT date FROM ForecastEntity where cityId = :cityID order by date limit 10)")
    fun purgeForecast(cityID: Int) : Completable

    /////////////////////////////////////////////////////////////////////////////////////////////
    //Search
    @Insert
    fun insertCitySearch(city: SearchEntity): Completable

    @Query("SELECT name FROM SearchEntity ORDER BY id DESC")
    fun getSearchList(): Single<MutableList<String>>

    @Query("DELETE FROM SearchEntity where id NOT IN (SELECT id from SearchEntity ORDER BY id DESC LIMIT 10)")
    fun purgeCitySearch() : Completable
}