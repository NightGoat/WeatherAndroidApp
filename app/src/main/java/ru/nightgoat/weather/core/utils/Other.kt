package ru.nightgoat.weather.core.utils

fun pressureFromHPaToMmHg(value: Int) = (value / pressureFromHPaToMmHgDiv).toInt().toString()

/**
 * Helper nullability function
 */
inline fun <reified T> T?.orIfNull(input: () -> T): T {
    return this ?: input()
}