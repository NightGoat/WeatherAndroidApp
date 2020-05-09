package ru.nightgoat.weather.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.nightgoat.weather.R
import ru.nightgoat.weather.di.components.DaggerBroadcastReceiverProvider
import ru.nightgoat.weather.domain.Interactor
import ru.nightgoat.weather.presentation.MainActivity
import ru.nightgoat.weather.presentation.city.CityFragment
import ru.nightgoat.weather.utils.chooseIcon
import ru.nightgoat.weather.utils.chooseUnits
import ru.nightgoat.weather.utils.convertToImg
import ru.nightgoat.weather.utils.getApiKey
import timber.log.Timber
import javax.inject.Inject

class GoogleLikeWidgetProvider : AppWidgetProvider() {
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
                    R.layout.widget_google_like
                )
                onTempClickListener(views, context)
                onIconClickListener(views, context)
                onDateClickListener(views, context)
                updateWeather(views, context, appWidgetManager, appWidgetId)
                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }

        }
    }

    private fun onDateClickListener(views: RemoteViews, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.type = "vnd.android.cursor.item/event"
        views.setOnClickPendingIntent(
            R.id.oneLineWidget_date,
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
            R.id.oneLineWidget_icon,
            PendingIntent
                .getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0
                )
        )
    }

    private fun onTempClickListener(views: RemoteViews, context: Context) {
        views.setOnClickPendingIntent(
            R.id.oneLineWidget_temp,
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
    private fun updateWeather(
        views: RemoteViews,
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int
    ) {

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
                    R.id.oneLineWidget_temp,
                    it.temp.toString().plus(degree)
                )
                views.setImageViewBitmap(
                    R.id.oneLineWidget_icon,
                    convertToImg(
                        chooseIcon(
                            it.iconId,
                            it.date,
                            it.sunrise,
                            it.sunset,
                            context
                        ), context, 30F
                    )
                )
                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }, {
                Timber.e(it)
            })
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Timber.tag(TAG).d("Broadcast received")
        context?.let { mContext ->
            sharedPreferences = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val temp = intent?.getIntExtra("temp", 0)
            val icon = intent?.getStringExtra("icon").toString()
            val views = RemoteViews(mContext.packageName, R.layout.widget_google_like)
            val degree = when (chooseUnits(sharedPreferences)) {
                "metric" -> context.getString(R.string.celsius)
                else -> context.getString(R.string.fahrenheit)
            }
            views.setTextViewText(
                R.id.oneLineWidget_temp,
                temp.toString().plus(degree)
            )
            views.setImageViewBitmap(
                R.id.oneLineWidget_icon,
                convertToImg(icon, mContext, 30F)
            )
            AppWidgetManager.getInstance(mContext).updateAppWidget(
                ComponentName(mContext, GoogleLikeWidgetProvider::class.java), views
            )
        }
    }

    companion object {
        val TAG = GoogleLikeWidgetProvider::class.java.simpleName
    }
}