package ru.nightgoat.weather.presentation.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import kotlinx.android.synthetic.main.fragment_settings.*

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import ru.nightgoat.weather.R
import ru.nightgoat.weather.presentation.WelcomeActivity
import ru.nightgoat.weather.utils.API_KEY
import ru.nightgoat.weather.utils.DEGREE_KEY
import ru.nightgoat.weather.utils.PRESSURE_KEY
import ru.nightgoat.weather.utils.SETTINGS_KEY

class SettingsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = context?.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)!!
        checkRadioGroupDegreeAndSetListener()
        checkRadioGroupPressureAndSetListener()
        putApiKeyAndSetClickListenerInEdit()
        settings_textInputLayout_api.setEndIconOnClickListener {
            val intent = Intent(activity, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun putApiKeyAndSetClickListenerInEdit() {
        settings_edit_api.setText(sharedPreferences.getString(API_KEY, ""))
        settings_edit_api.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                settings_edit_api.clearFocus()
                sharedPreferences.edit()
                    .putString(API_KEY, settings_edit_api.text.toString()).apply()
            }
            false
        }
    }

    private fun checkRadioGroupPressureAndSetListener() {
        settings_radGrpPressure.check(
            sharedPreferences.getInt(
                PRESSURE_KEY,
                R.id.settings_radBtnMmHg
            )
        )
        settings_radGrpPressure.setOnCheckedChangeListener { _, checkedId ->
            sharedPreferences
                .edit()
                .putInt(PRESSURE_KEY, checkedId)
                .apply()
        }
    }

    private fun checkRadioGroupDegreeAndSetListener() {
        settings_radGrpDegree.check(sharedPreferences.getInt(DEGREE_KEY, R.id.settings_radBtnCelsius))
        settings_radGrpDegree.setOnCheckedChangeListener { _, checkedId ->
            sharedPreferences
                .edit()
                .putInt(DEGREE_KEY, checkedId)
                .apply()
        }
    }

}