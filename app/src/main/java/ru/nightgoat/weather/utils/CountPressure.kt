package ru.nightgoat.weather.utils

fun pressureFromHPaToMmHg(value: Int) = (value / pressureFromHPaToMmHgDiv).toInt().toString()

