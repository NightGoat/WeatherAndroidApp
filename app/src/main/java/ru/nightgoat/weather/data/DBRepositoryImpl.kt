package ru.nightgoat.weather.data

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.nightgoat.weather.data.db.CitiesDao
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.data.entity.ForecastEntity
import ru.nightgoat.weather.data.entity.SearchEntity
import ru.nightgoat.weather.domain.DBRepository

class DBRepositoryImpl(val dao: CitiesDao) : DBRepository {

    override fun getAllCities(): Flowable<MutableList<CityEntity>> {
        return dao.getAllCities()
    }

    override fun getAllCitiesSingle(): Single<MutableList<CityEntity>> {
        return dao.getAllCities2()
    }

    override fun getCity(name: String): Maybe<CityEntity> {
        return dao.getCity(name)
    }

    override fun getCityById(cityId: Int): Maybe<CityEntity> {
        return dao.getCityById(cityId)
    }

    override fun insertCity(city: CityEntity): Completable {
        return dao.insertCity(city)
    }

    override fun deleteCity(city: CityEntity): Completable {
        return dao.deleteCity(city)
    }

    override fun updateCity(city: CityEntity): Completable {
        return dao.updateCity(city)
    }

    override fun getSearchList(): Single<MutableList<String>> {
        return dao.getSearchList()
    }

    override fun insertSearchEntity(city: SearchEntity): Completable {
        return dao.insertCitySearch(city)
    }

    override fun purgeSearchList(): Completable {
        return dao.purgeCitySearch()
    }

    override fun getForecast(cityId: Int): Flowable<MutableList<ForecastEntity>> {
        return dao.getForecast(cityId = cityId)
    }

    override fun insertForecast(forecast: ForecastEntity): Completable {
        return dao.insertForecast(forecast)
    }

    override fun deleteAllForecastForCity(cityId: Int): Completable {
        return dao.deleteForecast(cityId)
    }

}