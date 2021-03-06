package ru.nightgoat.weather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity

@Database(entities = [CityEntity::class, SearchEntity::class, ForecastEntity::class], version = 12, exportSchema = false)
abstract class CitiesDatabase : RoomDatabase() {
    abstract fun dao(): CitiesDao

    companion object {

        private const val DB_NAME = "Cities.db"

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
                CitiesDatabase::class.java, DB_NAME
            )
                .addMigrations(MIGRATION_10_11, MIGRATION_11_12)
                .build()
        }

        private val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE ForecastEntity")
                database.execSQL(
                    "CREATE TABLE ForecastEntity (" +
                            "cityId INTEGER NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "date INTEGER NOT NULL, " +
                            "temp INTEGER NOT NULL, " +
                            "iconId INTEGER NOT NULL, " +
                            "PRIMARY KEY(cityId, date))")
            }
        }

        private val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE CityEntity ADD COLUMN country TEXT")
            }
        }
    }
}
