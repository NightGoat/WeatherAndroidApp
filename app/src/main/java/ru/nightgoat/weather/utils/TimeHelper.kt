package ru.nightgoat.weather.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

fun getCurrentDate(): String = DateTimeFormat.forPattern("yyyy-MM-dd").print(System.currentTimeMillis())
fun getSqlDate(dt: Long): String = DateTimeFormat.forPattern("yyyy-MM-dd").print(dt)
@ExperimentalStdlibApi
fun getNormalDateTime(dt: Long): String = "${DateTimeFormat.forPattern("EEEE").print(dt).capitalize(Locale.getDefault())} " +
        DateTimeFormat.shortDateTime().withLocale(Locale.getDefault()).print(dt)
fun getHour(dt: Long): Int = DateTime(dt).hourOfDay
@ExperimentalStdlibApi
fun getDayOfWeekAndDate(dt: Long): String = DateTimeFormat.forPattern("EEEE, d MMM").print(dt).capitalize(Locale.getDefault())
