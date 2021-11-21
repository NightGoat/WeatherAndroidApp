package ru.nightgoat.weather.core.utils

fun pressureFromHPaToMmHg(value: Int) = (value / pressureFromHPaToMmHgDiv).toInt().toString()
fun Double?.toIntOrZero() = (this ?: 0.0).toInt()