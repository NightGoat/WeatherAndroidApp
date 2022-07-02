package ru.nightgoat.weather.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import dagger.android.AndroidInjection
import io.github.nightgoat.kexcore.logIfNull
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.extentions.getUnits
import ru.nightgoat.weather.core.utils.ICON_KEY
import ru.nightgoat.weather.core.utils.METRIC
import ru.nightgoat.weather.core.utils.TEMP_KEY
import ru.nightgoat.weather.domain.IInteractor
import ru.nightgoat.weather.presentation.MainActivity
import ru.nightgoat.weather.presentation.base.BaseAppWidgetProvider
import timber.log.Timber
import javax.inject.Inject

class GoogleLikeWidgetProvider : BaseAppWidgetProvider() {

    @Inject
    lateinit var interactor: IInteractor

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        context?.let {
            AndroidInjection.inject(this, context)
            context.setSharedPreferences()

            appWidgetIds?.let {
                val views = RemoteViews(
                    context.packageName,
                    R.layout.widget_google_like
                )
                onTempClickListener(views, context)
                onIconClickListener(views, context)
                onDateClickListener(views, context)

                appWidgetIds.forEach { appWidgetId ->
                    updateWeather(views, context, appWidgetManager, appWidgetId)
                    appWidgetManager?.updateAppWidget(appWidgetId, views)
                }
            }.logIfNull("onUpdate: appWidgetIds null")
        }.logIfNull("onUpdate: context null")

    }

    private fun onDateClickListener(views: RemoteViews, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.type = DATE_CLICK_INTENT
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

    private fun updateWeather(
        views: RemoteViews,
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int
    ) {
        val degree = when (units) {
            METRIC -> context.getString(R.string.celsius)
            else -> context.getString(R.string.fahrenheit)
        }
        interactor.getCityFromDataBaseAndUpdateFromApi(
            cityId = cityId,
            units = units,
            apiKey = apiKey
        ).observeOn(AndroidSchedulers.mainThread(), true)
            .subscribe({ city ->
                views.setTextViewText(
                    R.id.oneLineWidget_temp,
                    "${city.temp}$degree"
                )
                val icon = getWeatherIcon(
                    id = city.iconId,
                    dt = city.date,
                    sunrise = city.sunrise,
                    sunset = city.sunset,
                    context
                )
                val img = convertToImg(icon, SMALL_TEXT_SIZE, context)
                views.setImageViewBitmap(
                    R.id.oneLineWidget_icon,
                    img
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
            mContext.setSharedPreferences()
            val temp = intent?.getIntExtra(TEMP_KEY, 0)
            val icon = intent?.getStringExtra(ICON_KEY)
            val views = RemoteViews(mContext.packageName, R.layout.widget_google_like)
            val degree = when (sharedPreferences.getUnits()) {
                METRIC -> context.getString(R.string.celsius)
                else -> context.getString(R.string.fahrenheit)
            }
            views.setTextViewText(
                R.id.oneLineWidget_temp,
                "$temp$degree"
            )
            if (!icon.isNullOrEmpty()) {
                val img = convertToImg(icon, SMALL_TEXT_SIZE, context)
                views.setImageViewBitmap(
                    R.id.oneLineWidget_icon,
                    img
                )
            }
            AppWidgetManager.getInstance(mContext).updateAppWidget(
                ComponentName(mContext, GoogleLikeWidgetProvider::class.java), views
            )
        }
    }

    companion object {
        val TAG: String = GoogleLikeWidgetProvider::class.java.simpleName
        private const val DATE_CLICK_INTENT = "vnd.android.cursor.item/event"
        private const val SMALL_TEXT_SIZE = 30F
    }
}