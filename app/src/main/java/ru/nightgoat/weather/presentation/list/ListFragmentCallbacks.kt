package ru.nightgoat.weather.presentation.list

import ru.nightgoat.weather.data.entity.CityEntity

interface ListFragmentCallbacks {
    fun setCurrentCityAndCallCityFragment(cityName: String, cityId: Int)
    fun getWeatherIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String
    fun getColor(cityEntity: CityEntity): Int
    fun swapPositionWithFirst(city: CityEntity)
    fun updateCity(city: CityEntity)
}
