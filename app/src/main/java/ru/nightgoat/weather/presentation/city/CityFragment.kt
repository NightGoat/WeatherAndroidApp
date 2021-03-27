package ru.nightgoat.weather.presentation.city

import android.content.Intent
import android.graphics.Typeface
import kotlinx.android.synthetic.main.fragment_city.*

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nightgoat.weather.R
import ru.nightgoat.weather.presentation.base.BaseFragment
import ru.nightgoat.weather.utils.METRIC
import ru.nightgoat.weather.utils.PERCENT
import ru.nightgoat.weather.utils.getApiKey
import ru.nightgoat.weather.utils.getNormalDateTime
import ru.nightgoat.weather.widget.BigWidgetProvider
import ru.nightgoat.weather.widget.GoogleLikeWidgetProvider
import javax.inject.Inject

class CityFragment : BaseFragment(), CityFragmentCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = ForecastAdapter(this)

    private val apiKey by lazy {
        getApiKey(sharedPreferences)
    }

    private val viewModel: CityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFont()
        initList()
        observeViewModel()
        with (viewModel) {
            loadWeather(chooseCityId(), chooseUnits(), apiKey)
            city_swipeRefreshLayout.setOnRefreshListener {
                loadWeather(chooseCityId(), chooseUnits(), apiKey)
            }
        }
    }

    private fun initList() {
        city_recycler.layoutManager = LinearLayoutManager(context)
        city_recycler.adapter = adapter
        city_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun setFont() {
        context?.let {
            humidityIcon.typeface =
                Typeface.createFromAsset(it.assets, FONTS_PATH)
            pressureIcon.typeface =
                Typeface.createFromAsset(it.assets, FONTS_PATH)
            windIcon.typeface =
                Typeface.createFromAsset(it.assets, FONTS_PATH)
            city_text_weatherIcon.typeface =
                Typeface.createFromAsset(it.assets, FONTS_PATH)
        }

    }

    @ExperimentalStdlibApi
    private fun observeViewModel() {
        lateinit var degree: String
        with (viewModel) {
            cityLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                text_name.text = it.name
                city_text_country.text = it.country
                degree = if (chooseUnits() == METRIC) context?.getString(R.string.celsius).toString()
                else context?.getString(R.string.fahrenheit).toString()
                text_temp.text = it.temp.toString().plus(degree)
                text_feelsLike.text =
                    resources.getString(R.string.feelsLike, it.feelsTemp.toString()).plus(degree)
                text_temp_max.text = resources.getString(R.string.max, it.maxTemp.toString()).plus(degree)
                text_temp_min.text = resources.getString(R.string.min, it.minTemp.toString()).plus(degree)
                text_date.text = getNormalDateTime(it.date)
                text_humidity.text = it.humidity.toString().plus(PERCENT)
                text_pressure.text =
                    choosePressure(it.pressure)
                text_wind.text = it.wind.toString().plus(context?.getString(R.string.ms))
                city_text_description.text = it.description
                val icon = chooseIcon(it.iconId, it.date, it.sunrise, it.sunset)
                city_text_weatherIcon.text = icon
                val intentSmallWidget = Intent(requireContext(), GoogleLikeWidgetProvider::class.java)
                val intentBigWidget = Intent(requireContext(), BigWidgetProvider::class.java)
                intentSmallWidget.putExtra("temp", it.temp).putExtra("icon", icon)
                intentBigWidget.putExtra("temp", it.temp).putExtra("icon", icon)
                context?.sendBroadcast(intentSmallWidget)
                context?.sendBroadcast(intentBigWidget)

            })

            refreshLiveData.observe(viewLifecycleOwner, Observer {
                city_swipeRefreshLayout.isRefreshing = it
            })

            forecastLiveData.observe(viewLifecycleOwner, Observer {
                adapter.setList(it)
            })
        }
    }

    override fun getWeatherIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String {
        return chooseIcon(id, dt, sunrise, sunset)
    }

    override fun onStop() {
        super.onStop()
        viewModel.purgeForecast(chooseCityId())
    }

    companion object {
        private const val TAG = "CityFragment"
        private const val FONTS_PATH = "fonts/weathericons.ttf"
    }
}