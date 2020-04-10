package ru.nightgoat.weather.di.builder

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nightgoat.weather.di.ViewModelKey
import ru.nightgoat.weather.presentation.city.CityViewModel
import ru.nightgoat.weather.presentation.addCity.AddCityViewModel
import ru.nightgoat.weather.presentation.list.ListViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CityViewModel::class)
    abstract fun bindCityViewModel(newsViewModel: CityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListViewModel(listViewModel: ListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddCityViewModel::class)
    abstract fun bindAddCityViewModel(addCityViewModel: AddCityViewModel): ViewModel
}