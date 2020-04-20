package ru.nightgoat.weather.utils

import org.joda.time.format.DateTimeFormat
import java.util.*

@ExperimentalStdlibApi
fun getNormalDateTime(dt: Long): String = "${DateTimeFormat.forPattern("EEEE").print(dt).capitalize(Locale.getDefault())} " +
        DateTimeFormat.shortDateTime().withLocale(Locale.getDefault()).print(dt)
fun getNormalDateTimeNotCapitalized(dt: Long): String = "${DateTimeFormat.forPattern("EEEE").print(dt)} " +
        DateTimeFormat.shortDateTime().withLocale(Locale.getDefault()).print(dt)
@ExperimentalStdlibApi
fun getDayOfWeekAndDate(dt: Long): String = DateTimeFormat.forPattern("EEEE, d MMM").print(dt).capitalize(Locale.getDefault())
