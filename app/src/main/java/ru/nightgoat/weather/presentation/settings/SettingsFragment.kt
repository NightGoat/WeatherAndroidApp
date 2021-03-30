package ru.nightgoat.weather.presentation.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.delegates.viewBinding
import ru.nightgoat.weather.core.utils.API_KEY
import ru.nightgoat.weather.core.utils.DEGREE_KEY
import ru.nightgoat.weather.core.utils.PRESSURE_KEY
import ru.nightgoat.weather.core.utils.getApiKey
import ru.nightgoat.weather.databinding.FragmentSettingsBinding
import ru.nightgoat.weather.presentation.WelcomeActivity
import ru.nightgoat.weather.presentation.base.BaseFragment

class SettingsFragment : BaseFragment() {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkRadioGroupDegreeAndSetListener()
        checkRadioGroupPressureAndSetListener()
        putApiKeyAndSetClickListenerInEdit()
        binding.settingsTextInputLayoutApi.setEndIconOnClickListener {
            val intent = Intent(activity, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun putApiKeyAndSetClickListenerInEdit() {
        sharedPreferences?.run {
            val key = getApiKey()
            binding.settingsEditApi.run {
                setText(key)
                onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
                    saveApiKey()
                }
            }
        }
    }

    private fun SharedPreferences.saveApiKey() {
        val newKey = binding.settingsEditApi.text.toString()
        edit().putString(API_KEY, newKey).apply()
    }

    private fun checkRadioGroupPressureAndSetListener() {
        sharedPreferences?.run {
            binding.settingsRadGrpPressure.check(
                getInt(
                    PRESSURE_KEY,
                    R.id.settings_radBtnMmHg
                )
            )
            binding.settingsRadGrpPressure.setOnCheckedChangeListener { _, checkedId ->
                edit().putInt(PRESSURE_KEY, checkedId).apply()
            }
        }
    }

    private fun checkRadioGroupDegreeAndSetListener() {
        sharedPreferences?.run {
            binding.settingsRadGrpDegree.check(
                getInt(
                    DEGREE_KEY,
                    R.id.settings_radBtnCelsius
                )
            )
            binding.settingsRadGrpDegree.setOnCheckedChangeListener { _, checkedId ->
                edit().putInt(DEGREE_KEY, checkedId).apply()
            }
        }
    }

}