package ru.nightgoat.weather.core.utils

import org.joda.time.format.DateTimeFormat
import ru.nightgoat.kextensions.normalize
import java.util.*

fun getNormalDateTime(dt: Long): String =
    "${DateTimeFormat.forPattern(DATE_PATTERN_EEEE).print(dt).normalize()} " +
            DateTimeFormat.shortDateTime().withLocale(Locale.getDefault()).print(dt)

@ExperimentalStdlibApi
fun getDayOfWeekAndDate(dt: Long): String =
    DateTimeFormat.forPattern(DATE_PATTERN_EEEE_D_MMM).print(dt).normalize()
