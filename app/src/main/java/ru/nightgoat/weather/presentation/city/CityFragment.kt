package ru.nightgoat.weather.presentation.city

import android.content.Context
import android.content.SharedPreferences
import kotlinx.android.synthetic.main.fragment_city.*

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.nightgoat.weather.R
import ru.nightgoat.weather.di.components.CityFragmentComponent
import ru.nightgoat.weather.di.components.DaggerCityFragmentComponent
import ru.nightgoat.weather.utils.getNormalDateTime
import ru.nightgoat.weather.utils.pressureFromHPaToMmHg
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class CityFragment : Fragment() {

    @Inject
    lateinit var viewModel: CityViewModel

    lateinit var sharedPreferences: SharedPreferences

    private val injector: CityFragmentComponent = DaggerCityFragmentComponent
        .builder()
        .setFragment(this)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_city, container, false)
        injector.inject(this)
        sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
        viewModel.cityName = sharedPreferences.getString("cityName", "Kazan").toString()
        viewModel.units = chooseUnits()
        viewModel.loadWeather()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun chooseUnits(): String {
        return if (sharedPreferences.getInt("degree", R.id.settings_radBtnCelsius) == R.id.settings_radBtnCelsius) "metric"
        else "imperial"
    }

    private fun choosePressure(value: Int): String {
        return if (sharedPreferences.getInt("pressure", R.id.settings_radBtnMmHg)
            == R.id.settings_radBtnMmHg) pressureFromHPaToMmHg(value).plus(getString(R.string.mmHg)
        )
        else value.toString().plus(getString(R.string.hPa))
    }

    private fun observeViewModel() {
        viewModel.city.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            text_name.text = it.name
            text_temp.text = it.main.temp.toString()
            text_feelsLike.text =
                resources.getString(R.string.feelsLike, it.main.feelsLike.toString())
            text_temp_max.text = resources.getString(R.string.max, it.main.tempMax.toString())
            text_temp_min.text = resources.getString(R.string.min, it.main.tempMin.toString())
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png")
                .into(city_image_icon)
            text_date.text = getNormalDateTime(it.dt)
//            text_date.text = DateFormat.getDateTimeInstance(
//                DateFormat.SHORT,
//                DateFormat.SHORT,
//                Locale.getDefault()
//            ).format(Date(it.dt * 1000))
            text_humidity.text = it.main.humidity.toString()
            text_pressure.text =
                it.main.pressure.let { pressureValue -> choosePressure(pressureValue) }
            text_wind.text = it.wind.speed.toString()
        })
    }
}