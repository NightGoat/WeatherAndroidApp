package ru.nightgoat.weather.core.extentions

import android.content.Context
import android.graphics.Typeface
import ru.nightgoat.weather.core.utils.FONTS_PATH

fun Context.createTypeFace() = Typeface.createFromAsset(
    this.assets,
    FONTS_PATH
)