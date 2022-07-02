package ru.nightgoat.weather.core.utils

fun pressureFromHPaToMmHg(value: Int) = (value / PRESSURE_FROM_HPA_TO_MMHG_DIV).toInt().toString()
fun Double?.toIntOrZero() = (this ?: 0.0).toInt()