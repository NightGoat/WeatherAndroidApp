package ru.nightgoat.weather.presentation.city

import android.content.Context
import android.content.SharedPreferences
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
import androidx.recyclerview.widget.RecyclerView
import ru.nightgoat.weather.R
import ru.nightgoat.weather.presentation.base.BaseFragment
import ru.nightgoat.weather.presentation.list.ListAdapter
import ru.nightgoat.weather.utils.getNormalDateTime
import ru.nightgoat.weather.utils.pressureFromHPaToMmHg
import javax.inject.Inject

class CityFragment : BaseFragment(), CityFragmentCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapter = ForecastAdapter(this)

    private val viewModel: CityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_city, container, false)
        sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
        viewModel.loadWeather(chooseCityId(), chooseUnits())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFont()
        initList()
        observeViewModel()
        city_swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadWeather(chooseCityId(), chooseUnits())
        }
    }

    private fun initList() {
        city_recycler.layoutManager = LinearLayoutManager(context)
        city_recycler.adapter = adapter
        city_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun setFont() {
        humidityIcon.typeface =
            Typeface.createFromAsset(context!!.assets, "fonts/weathericons.ttf")
        pressureIcon.typeface =
            Typeface.createFromAsset(context!!.assets, "fonts/weathericons.ttf")
        windIcon.typeface =
            Typeface.createFromAsset(context!!.assets, "fonts/weathericons.ttf")
        city_text_weatherIcon.typeface =
            Typeface.createFromAsset(context!!.assets, "fonts/weathericons.ttf")
    }

    private fun observeViewModel() {
        lateinit var degree: String
        viewModel.cityLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            text_name.text = it.name
            degree = if (chooseUnits() == "metric") context?.getString(R.string.celsius).toString()
            else context?.getString(R.string.fahrenheit).toString()
            text_temp.text = it.temp.toString().plus(degree)
            text_feelsLike.text =
                resources.getString(R.string.feelsLike, it.feelsTemp.toString()).plus(degree)
            text_temp_max.text = resources.getString(R.string.max, it.maxTemp.toString()).plus(degree)
            text_temp_min.text = resources.getString(R.string.min, it.minTemp.toString()).plus(degree)
            text_date.text = getNormalDateTime(it.date)
            text_humidity.text = it.humidity.toString().plus("%")
            text_pressure.text =
                it.pressure.let { pressureValue -> choosePressure(pressureValue) }
            text_wind.text = it.wind.toString().plus(context?.getString(R.string.ms))
            city_text_description.text = it.description
            city_text_weatherIcon.text = chooseIcon(it.iconId, it.date, it.sunrise, it.sunset)
        })
        viewModel.refreshLiveData.observe(viewLifecycleOwner, Observer {
            city_swipeRefreshLayout.isRefreshing = it
        })

        viewModel.forecastLiveData.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })
    }

    override fun getWeatherIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String {
        return chooseIcon(id, dt, sunrise, sunset)
    }

    companion object {
        @JvmStatic
        val TAG = CityFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = CityFragment().apply {
            arguments = Bundle()
        }
    }
}