package ru.nightgoat.weather.presentation.city

interface CityFragmentCallbacks {
    fun getWeatherIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String
}