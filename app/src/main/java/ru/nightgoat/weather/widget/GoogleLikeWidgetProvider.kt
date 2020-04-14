package ru.nightgoat.weather.widget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.RemoteViews
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.nightgoat.weather.R
import ru.nightgoat.weather.di.components.DaggerBroadcastReceiverProvider
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.utils.chooseIcon
import ru.nightgoat.weather.utils.chooseUnits
import ru.nightgoat.weather.utils.convertToImg
import ru.nightgoat.weather.utils.getApiKey
import javax.inject.Inject

class GoogleLikeWidgetProvider : AppWidgetProvider() {
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var interactor: Interactor

    @SuppressLint("CheckResult")
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        if (context != null) {
            DaggerBroadcastReceiverProvider.builder().context(context).build().inject(this)
        }
        sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
        val degree = when (chooseUnits(sharedPreferences)) {
            "metric" -> context.getString(R.string.celsius)
            else -> context.getString(R.string.fahrenheit)
        }
        if (appWidgetIds != null) {
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName,
                    R.layout.widget_google_like
                )
                interactor.getCityFromDataBaseAndUpdateFromApi(
                    sharedPreferences.getInt("cityId", 551487), chooseUnits(sharedPreferences), getApiKey(sharedPreferences)
                )
                    .observeOn(AndroidSchedulers.mainThread(), true)
                    .subscribe({
                        Log.d("WIDGET", it.name)
                        views.setTextViewText(
                            R.id.oneLineWidget_temp,
                            it.temp.toString().plus(degree)
                        )
                        Log.d(
                            "WIDGET",
                            chooseIcon(it.iconId, it.date, it.sunrise, it.sunset, context)
                        )
                        views.setImageViewBitmap(
                            R.id.oneLineWidget_icon,
                            convertToImg(chooseIcon(it.iconId, it.date, it.sunrise, it.sunset, context), context)
                        )
                        appWidgetManager?.updateAppWidget(appWidgetId, views)
                    }, {
                        Log.e("WIDGET", it.message.toString())
                    })
            }

        }
    }


}