package ru.nightgoat.weather.presentation.settings

import android.content.Context
import kotlinx.android.synthetic.main.fragment_settings.*

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.nightgoat.weather.R
import ru.nightgoat.weather.di.components.DaggerSettingsFragmentComponent
import javax.inject.Inject

class SettingsFragment : Fragment() {

    private val sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings_radGrpDegree.setOnCheckedChangeListener { _, checkedId ->
            sharedPreferences
                ?.edit()
                ?.putInt("degree", checkedId)
                ?.apply()
        }

        settings_radGrpPressure.setOnCheckedChangeListener { _, checkedId ->
            sharedPreferences
                ?.edit()
                ?.putInt("pressure", checkedId)
                ?.apply()
        }
    }

}