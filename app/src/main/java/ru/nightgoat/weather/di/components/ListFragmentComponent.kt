package ru.nightgoat.weather.di.components

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Component
import ru.nightgoat.weather.di.ActivityScope
import ru.nightgoat.weather.di.modules.ListFragmentModule
import ru.nightgoat.weather.presentation.list.ListFragment

@ActivityScope
@Component(modules = [(ListFragmentModule::class)])
interface ListFragmentComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setFragment(fragment: Fragment): Builder
        fun build(): ListFragmentComponent
    }

    fun inject(fragment: ListFragment)
}
