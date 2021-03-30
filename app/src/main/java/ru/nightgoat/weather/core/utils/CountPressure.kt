package ru.nightgoat.weather.core.utils

fun pressureFromHPaToMmHg(value: Int) = (value / pressureFromHPaToMmHgDiv).toInt().toString()

