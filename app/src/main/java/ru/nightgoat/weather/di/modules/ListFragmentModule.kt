package ru.nightgoat.weather.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.nightgoat.weather.di.ActivityScope
import ru.nightgoat.weather.presentation.list.ListViewModel

@Module
object ListFragmentModule {

    @Provides
    @ActivityScope
    @JvmStatic
    internal fun provideListViewModel(fragment: Fragment, factory: ViewModelProvider.NewInstanceFactory): ListViewModel {
        return ViewModelProvider(fragment, factory).get(ListViewModel::class.java)
    }

    @Provides
    @ActivityScope
    @JvmStatic
    internal fun provideViewModelFactory(): ViewModelProvider.NewInstanceFactory {
        return ViewModelProvider.NewInstanceFactory()
    }
}