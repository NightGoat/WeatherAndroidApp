package ru.nightgoat.weather.core.utils

import timber.log.Timber

fun pressureFromHPaToMmHg(value: Int) = (value / pressureFromHPaToMmHgDiv).toInt().toString()

/**
 * Helper nullability function
 */
inline fun <reified T> T?.orIfNull(input: () -> T): T {
    return this ?: input()
}

fun <T: Any>T?.logIfNull(message: String): T? {
    if (this == null) {
        Timber.e(message)
    }
    return this
}