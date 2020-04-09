package ru.nightgoat.weather.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjection
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.nightgoat.weather.App
import ru.nightgoat.weather.di.builder.ActivityBuilder
import ru.nightgoat.weather.di.builder.ViewModelBuilder
import ru.nightgoat.weather.di.modules.ContextModule
import ru.nightgoat.weather.di.modules.DataModule
import ru.nightgoat.weather.di.modules.InteractorModule
import ru.nightgoat.weather.di.modules.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DataModule::class,
        ContextModule::class,
        NetworkModule::class,
        InteractorModule::class,
        ActivityBuilder::class,
        ViewModelBuilder::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

}
