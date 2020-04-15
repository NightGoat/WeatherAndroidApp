package ru.nightgoat.weather.di.components

import android.content.Context
import dagger.*
import ru.nightgoat.weather.widget.GoogleLikeWidgetProvider
import ru.nightgoat.weather.di.modules.DataModule
import ru.nightgoat.weather.di.modules.InteractorModule
import ru.nightgoat.weather.di.modules.NetworkModule
import ru.nightgoat.weather.widget.BigWidgetProvider
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, InteractorModule::class, DataModule::class])
interface BroadcastReceiverProvider {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): BroadcastReceiverProvider
    }

    fun inject(provider: GoogleLikeWidgetProvider)
    fun inject(provider: BigWidgetProvider)
}