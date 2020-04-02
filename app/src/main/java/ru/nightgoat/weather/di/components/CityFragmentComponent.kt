package ru.nightgoat.weather.di.components

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Component
import ru.nightgoat.weather.di.ActivityScope
import ru.nightgoat.weather.di.modules.CityFragmentModule
import ru.nightgoat.weather.presentation.city.CityFragment

@ActivityScope
@Component(modules = [(CityFragmentModule::class)])
interface CityFragmentComponent {

    @Component.Builder
    interface Builder {
        fun build(): CityFragmentComponent
        @BindsInstance
        fun setFragment(fragment: Fragment): Builder
    }

    fun inject(fragment: CityFragment)
}
