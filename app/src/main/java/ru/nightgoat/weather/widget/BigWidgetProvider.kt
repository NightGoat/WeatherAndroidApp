package ru.nightgoat.weather.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.util.Log
import android.widget.RemoteViews
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.nightgoat.weather.R
import ru.nightgoat.weather.di.components.DaggerBroadcastReceiverProvider
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.presentation.MainActivity
import ru.nightgoat.weather.utils.chooseIcon
import ru.nightgoat.weather.utils.chooseUnits
import ru.nightgoat.weather.utils.convertToImg
import ru.nightgoat.weather.utils.getApiKey
import javax.inject.Inject

class BigWidgetProvider : AppWidgetProvider() {
    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var interactor: Interactor

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        if (context != null) {
            DaggerBroadcastReceiverProvider.builder().context(context).build().inject(this)
        }
        sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!

        if (appWidgetIds != null) {
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(
                    context.packageName,
                    R.layout.widget_big
                )
                onTempClickListener(views, context)
                onIconClickListener(views, context)
                onDateClickListener(views, context)
                onTimeClickListener(views, context)
                updateWeather(views, context, appWidgetManager, appWidgetId)
                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }
        }
    }

    private fun onTimeClickListener(views: RemoteViews, context: Context) {
        views.setOnClickPendingIntent(
            R.id.twoLineWidget_clock,
            PendingIntent.getActivity(
                context,
                0,
                Intent(AlarmClock.ACTION_SHOW_ALARMS),
                0
            )
        )
    }

    private fun onDateClickListener(views: RemoteViews, context: Context) {

        val builder = CalendarContract.CONTENT_URI.buildUpon()
        builder.appendPath("time")
        ContentUris.appendId(builder, System.currentTimeMillis())
        val intent = Intent(Intent.ACTION_VIEW).setData(builder.build())
        views.setOnClickPendingIntent(
            R.id.twoLineWidget_date,
            PendingIntent.getActivity(
                context,
                0,
                intent,
                0
            )
        )
    }

    private fun onIconClickListener(views: RemoteViews, context: Context) {
        views.setOnClickPendingIntent(
            R.id.twoLineWidget_icon,
            PendingIntent
                .getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0
                )
        )
    }

    private fun onTempClickListener(views : RemoteViews, context: Context) {
        views.setOnClickPendingIntent(
            R.id.twoLineWidget_temp,
            PendingIntent
                .getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0
                )
        )
    }

    @SuppressLint("CheckResult")
    private fun updateWeather(views: RemoteViews, context: Context, appWidgetManager: AppWidgetManager?, appWidgetId: Int) {
        val degree = when (chooseUnits(sharedPreferences)) {
            "metric" -> context.getString(R.string.celsius)
            else -> context.getString(R.string.fahrenheit)
        }
        interactor.getCityFromDataBaseAndUpdateFromApi(
            sharedPreferences.getInt("cityId", 551487),
            chooseUnits(sharedPreferences),
            getApiKey(sharedPreferences)
        )
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribe({
                views.setTextViewText(
                    R.id.twoLineWidget_temp,
                    it.temp.toString().plus(degree)
                )
                views.setImageViewBitmap(
                    R.id.twoLineWidget_icon,
                    convertToImg(
                        chooseIcon(
                            it.iconId,
                            it.date,
                            it.sunrise,
                            it.sunset,
                            context
                        ), context, 128F
                    )
                )
                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }, {
                Log.e("big WIDGET", it.message.toString())
            })
    }
}