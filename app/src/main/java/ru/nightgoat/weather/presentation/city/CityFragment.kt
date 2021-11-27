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
import ru.nightgoat.kextensions.android.setInvisible
import ru.nightgoat.kextensions.normalize
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.delegates.viewBinding
import ru.nightgoat.weather.core.utils.*
import ru.nightgoat.weather.data.entity.CityEntity
import ru.nightgoat.weather.databinding.FragmentCityBinding
import ru.nightgoat.weather.presentation.base.BaseFragment
import ru.nightgoat.weather.widget.BigWidgetProvider
import ru.nightgoat.weather.widget.GoogleLikeWidgetProvider
import ru.nightgoat.weather.widget.SmallWidgetProvider
import javax.inject.Inject

class CityFragment : BaseFragment(), CityFragmentCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val binding: FragmentCityBinding by viewBinding()

    private val forecastAdapter = ForecastAdapter(this)

    private val viewModel: CityViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

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
        initShimmeringRecycler()
        initForecastRecycler()
    }

    private fun initForecastRecycler() {
        with(binding.cityRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = forecastAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun initShimmeringRecycler() {
        with(binding.fakeRecycler) {
            val shadapter = ShimmerAdapter()
            adapter = shadapter
            layoutManager = LinearLayoutManager(context)
            shadapter.notifyDataSetChanged()
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

    private fun observeViewModel() {
        with(viewModel) {
            observeCityData()
            observeLoading()
            observeForecast()
        }
    }

    private fun CityViewModel.observeForecast() {
        forecastLiveData.observe(viewLifecycleOwner, {
            forecastAdapter.setList(it)
        })
    }

    private fun CityViewModel.observeLoading() {
        refreshLiveData.observe(viewLifecycleOwner, { isLoading ->
            with(binding) {
                citySwipeRefreshLayout.isRefreshing = isLoading
                listOf(
                    constraintTopTemp,
                    constraintTopCity,
                    constraintParameters,
                    cityRecycler
                ).forEach { layout ->
                    layout.setInvisible(isLoading)
                }

                listOf(
                    shimmerTopTemp,
                    shimmerCity,
                    shimmerParameters,
                    shimmerRecycler
                ).forEach { shimmer ->
                    shimmer.setInvisible(!isLoading)
                }
            }
        })
    }

    private fun CityViewModel.observeCityData() {
        cityLiveData.observe(viewLifecycleOwner, { city ->
            val icon = chooseIcon(city.iconId, city.date, city.sunrise, city.sunset)
            setDataToScreen(city, icon)
            sendDataToWidgets(city, icon)
        })
    }


    private fun setDataToScreen(city: CityEntity, icon: String) {
        with(binding) {
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
            cityTextDescription.text = city.description.normalize()
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
        requireContext().run {
            val intentGoogleLikeWidget = Intent(this, GoogleLikeWidgetProvider::class.java)
            val intentBigWidget = Intent(this, BigWidgetProvider::class.java)
            val intentSmallWidget = Intent(this, SmallWidgetProvider::class.java)
            intentSmallWidget.putExtra(TEMP_KEY, city.temp).putExtra(ICON_KEY, icon)
            intentBigWidget.putExtra(TEMP_KEY, city.temp).putExtra(ICON_KEY, icon)
            intentGoogleLikeWidget.putExtra(TEMP_KEY, city.temp).putExtra(ICON_KEY, icon)
            sendBroadcast(intentSmallWidget)
            sendBroadcast(intentGoogleLikeWidget)
            sendBroadcast(intentBigWidget)
        }
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