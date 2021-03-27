package ru.nightgoat.weather.presentation.city

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_city.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.presentation.base.BaseFragment
import ru.nightgoat.weather.utils.*
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
        loadWeather()
    }

    private fun loadWeather(){
        val units = chooseUnits()
        val cityId = chooseCityId()
        with (viewModel) {
            loadWeather(cityId, units, apiKey)
            city_swipeRefreshLayout.setOnRefreshListener {
                loadWeather(cityId, units, apiKey)
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
            val localAssets = it.assets
            humidityIcon.typeface =
                Typeface.createFromAsset(localAssets, FONTS_PATH)
            pressureIcon.typeface =
                Typeface.createFromAsset(localAssets, FONTS_PATH)
            windIcon.typeface =
                Typeface.createFromAsset(localAssets, FONTS_PATH)
            city_text_weatherIcon.typeface =
                Typeface.createFromAsset(localAssets, FONTS_PATH)
        }
    }

    @ExperimentalStdlibApi
    private fun observeViewModel() {
        with (viewModel) {
            cityLiveData.observe(viewLifecycleOwner, { city ->
                val icon = chooseIcon(city.iconId, city.date, city.sunrise, city.sunset)
                setDataToScreen(city, icon)
                sendDataToWidgets(city, icon)
            })

            refreshLiveData.observe(viewLifecycleOwner, {
                city_swipeRefreshLayout.isRefreshing = it
            })

            forecastLiveData.observe(viewLifecycleOwner, {
                adapter.setList(it)
            })
        }
    }

    @ExperimentalStdlibApi
    private fun setDataToScreen(city: CityEntity, icon: String) {
        text_name.text = city.name
        city_text_country.text = city.country
        val degree = getDegree()
        text_temp.text = getString(R.string.valuePlusParam, city.temp, degree)
        text_feelsLike.text = getString(R.string.feelsLike, city.feelsTemp.toString(), degree)
        text_temp_max.text = getString(R.string.max, city.maxTemp.toString(), degree)
        text_temp_min.text = getString(R.string.min, city.minTemp.toString(), degree)
        text_date.text = getNormalDateTime(city.date)
        text_humidity.text = getString(R.string.valuePlusParam, city.humidity, PERCENT)
        text_pressure.text = choosePressure(city.pressure)
        text_wind.text = getString(R.string.windWithMs, city.wind)
        city_text_description.text = city.description
        city_text_weatherIcon.text = icon
    }

    private fun getDegree(): String {
        val units = chooseUnits()
        val degreeRes = if (units == METRIC) {
            R.string.celsius
        } else {
            R.string.fahrenheit
        }
        return getString(degreeRes)
    }

    private fun sendDataToWidgets(city: CityEntity, icon: String){
        val localContext = requireContext()
        val intentSmallWidget = Intent(localContext, GoogleLikeWidgetProvider::class.java)
        val intentBigWidget = Intent(localContext, BigWidgetProvider::class.java)
        intentSmallWidget.putExtra(TEMP_KEY, city.temp).putExtra(ICON_KEY, icon)
        intentBigWidget.putExtra(TEMP_KEY, city.temp).putExtra(ICON_KEY, icon)
        localContext.sendBroadcast(intentSmallWidget)
        localContext.sendBroadcast(intentBigWidget)
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