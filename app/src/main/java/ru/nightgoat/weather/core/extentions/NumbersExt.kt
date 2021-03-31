package ru.nightgoat.weather.core.extentions

fun Int?.orZero() = this ?: 0
fun Double?.toIntOrZero() = (this ?: 0.0).toInt()
fun Long?.orZero() = this ?: 0L