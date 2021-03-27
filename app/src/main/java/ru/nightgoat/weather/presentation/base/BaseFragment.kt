package ru.nightgoat.weather.presentation.base

import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_list.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.providers.IResManager
import ru.nightgoat.weather.utils.*
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var resManager: IResManager

    val sharedPreferences: SharedPreferences? by lazy {
        context?.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
    }

    private val navController by lazy {
        findNavController()
    }

    fun chooseCityId(): Int {
        return sharedPreferences?.getInt(CITY_ID_KEY, 0) ?: 0
    }

    fun chooseUnits(): String {
        return getUnits(sharedPreferences)
    }

    fun choosePressure(value: Int): String {
        val pressure = sharedPreferences?.getInt(PRESSURE_KEY, R.id.settings_radBtnMmHg)
        return if (pressure == R.id.settings_radBtnMmHg) {
            val mmHg = getString(R.string.mmHg)
            val newValue = pressureFromHPaToMmHg(value)
            "$newValue $mmHg"
        } else {
            val hPa = getString(R.string.hPa)
            "$value $hPa"
        }
    }

    fun chooseIcon(id: Int, dt: Long, sunrise: Long, sunset: Long): String {
        return resManager.getWeatherIcon(id, dt, sunrise, sunset)
    }

    fun navigateTo(action: Int) {
        navController.navigate(action)
    }

    fun showSnackBar(text: String, length: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(list_fab, text, length).show()
    }
}