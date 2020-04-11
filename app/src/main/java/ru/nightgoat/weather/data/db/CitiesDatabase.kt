package ru.nightgoat.weather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity

@Database(entities = [CityEntity::class, SearchEntity::class, ForecastEntity::class], version = 8, exportSchema = false)
abstract class CitiesDatabase : RoomDatabase() {
    abstract fun dao(): CitiesDao

    companion object {

        @Volatile
        private var instance: CitiesDatabase? = null

        fun getInstance(context: Context): CitiesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDataBase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDataBase(context: Context): CitiesDatabase {
            return Room.databaseBuilder(
                context,
                CitiesDatabase::class.java, "Cities.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}