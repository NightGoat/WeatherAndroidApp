package ru.nightgoat.weather.di.modules

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.nightgoat.weather.di.ActivityScope
import ru.nightgoat.weather.presentation.list.ListViewModel
import javax.inject.Singleton

@Module
object ContextModule {

    lateinit var context: Context;


    @Provides
    @Singleton
    @JvmStatic
    internal fun provideContext(fragment: Fragment, factory: ViewModelProvider.NewInstanceFactory): ListViewModel {
        return ViewModelProvider(fragment, factory).get(ListViewModel::class.java)
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideViewModelFactory(): ViewModelProvider.NewInstanceFactory {
        return ViewModelProvider.NewInstanceFactory()
    }
}