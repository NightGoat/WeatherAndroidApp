package ru.nightgoat.weather.utils

import org.joda.time.format.DateTimeFormat
import java.util.*

@ExperimentalStdlibApi
fun getNormalDateTime(dt: Long): String = "${DateTimeFormat.forPattern(DATE_PATTERN_EEEE).print(dt).capitalize(Locale.getDefault())} " +
        DateTimeFormat.shortDateTime().withLocale(Locale.getDefault()).print(dt)

fun getNormalDateTimeNotCapitalized(dt: Long): String = "${DateTimeFormat.forPattern(DATE_PATTERN_EEEE).print(dt)} " +
        DateTimeFormat.shortDateTime().withLocale(Locale.getDefault()).print(dt)

@ExperimentalStdlibApi
fun getDayOfWeekAndDate(dt: Long): String = DateTimeFormat.forPattern(DATE_PATTERN_EEEE_D_MMM).print(dt).capitalize(Locale.getDefault())
