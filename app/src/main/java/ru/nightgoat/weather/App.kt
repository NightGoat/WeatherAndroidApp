package ru.nightgoat.weather

import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import net.danlew.android.joda.JodaTimeAndroid
import ru.nightgoat.kextensions.utils.Kex
import ru.nightgoat.weather.di.components.DaggerAppComponent
import java.net.UnknownHostException

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        RxJavaPlugins.setErrorHandler { throwable ->
            if (throwable is UndeliverableException && throwable.cause is UnknownHostException) {
                return@setErrorHandler // ignore BleExceptions as they were surely delivered at least once
            }
            throw RuntimeException("Unexpected Throwable in RxJavaPlugins error handler", throwable)
        }
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Kex.setTimber()
        }
    }
}