package ru.nightgoat.weather.presentation.city

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.delegates.viewBinding
import ru.nightgoat.weather.core.utils.*
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.databinding.FragmentCityBinding
import ru.nightgoat.weather.presentation.base.BaseFragment
import ru.nightgoat.weather.widget.BigWidgetProvider
import ru.nightgoat.weather.widget.GoogleLikeWidgetProvider
import javax.inject.Inject

class CityFragment : BaseFragment(), CityFragmentCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val binding: FragmentCityBinding by viewBinding()

    private val adapter = ForecastAdapter(this)

    private val viewModel: CityViewModel by viewModels(factoryProducer = { viewModelFactory })

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

    private fun loadWeather() {
        with(viewModel) {
            loadWeather(cityId, units, apiKey)
            binding.citySwipeRefreshLayout.setOnRefreshListener {
                loadWeather(cityId, units, apiKey)
            }
        }
    }

    private fun initList() {
        with(binding.cityRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setFont() {
        context?.let {
            with(binding) {
                val localAssets = it.assets
                humidityIcon.typeface =
                    Typeface.createFromAsset(localAssets, FONTS_PATH)
                pressureIcon.typeface =
                    Typeface.createFromAsset(localAssets, FONTS_PATH)
                windIcon.typeface =
                    Typeface.createFromAsset(localAssets, FONTS_PATH)
                cityTextWeatherIcon.typeface =
                    Typeface.createFromAsset(localAssets, FONTS_PATH)
            }
        }
    }

    @ExperimentalStdlibApi
    private fun observeViewModel() {
        with(viewModel) {
            cityLiveData.observe(viewLifecycleOwner, { city ->
                val icon = chooseIcon(city.iconId, city.date, city.sunrise, city.sunset)
                setDataToScreen(city, icon)
                sendDataToWidgets(city, icon)
            })

            refreshLiveData.observe(viewLifecycleOwner, {
                binding.citySwipeRefreshLayout.isRefreshing = it
            })

            forecastLiveData.observe(viewLifecycleOwner, {
                adapter.setList(it)
            })
        }
    }

    @ExperimentalStdlibApi
    private fun setDataToScreen(city: CityEntity, icon: String) {
        with(binding){
            textName.text = city.name
            cityTextCountry.text = city.country
            val degree = getDegree()
            textTemp.text = getString(R.string.valuePlusParam, city.temp, degree)
            textFeelsLike.text =
                getString(R.string.feelsLike, city.feelsTemp.toString(), degree)
            textTempMax.text = getString(R.string.max, city.maxTemp.toString(), degree)
            textTempMin.text = getString(R.string.min, city.minTemp.toString(), degree)
            textDate.text = getNormalDateTime(city.date)
            textHumidity.text = getString(R.string.valuePlusParam, city.humidity, PERCENT)
            textPressure.text = choosePressure(city.pressure)
            textWind.text = getString(R.string.windWithMs, city.wind)
            cityTextDescription.text = city.description
            cityTextWeatherIcon.text = icon
        }
    }

    private fun getDegree(): String {
        val degreeRes = if (units == METRIC) {
            R.string.celsius
        } else {
            R.string.fahrenheit
        }
        return getString(degreeRes)
    }

    private fun sendDataToWidgets(city: CityEntity, icon: String) {
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
        viewModel.purgeForecast(cityId)
    }

    companion object {
        private const val TAG = "CityFragment"
        private const val FONTS_PATH = "fonts/weathericons.ttf"
    }
}