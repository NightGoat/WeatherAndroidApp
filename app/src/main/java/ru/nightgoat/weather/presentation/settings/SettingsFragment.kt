package ru.nightgoat.weather.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.nightgoat.weather.R
import ru.nightgoat.weather.presentation.WelcomeActivity
import ru.nightgoat.weather.presentation.base.BaseFragment
import ru.nightgoat.weather.utils.API_KEY
import ru.nightgoat.weather.utils.DEGREE_KEY
import ru.nightgoat.weather.utils.PRESSURE_KEY

class SettingsFragment : BaseFragment() {

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
        settings_textInputLayout_api.setEndIconOnClickListener {
            val intent = Intent(activity, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun putApiKeyAndSetClickListenerInEdit() {
        sharedPreferences?.run {
            val key = getString(API_KEY, "")
            settings_edit_api.setText(key)
            settings_edit_api.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    settings_edit_api.clearFocus()
                    val newKey = settings_edit_api.text.toString()
                    edit().putString(API_KEY, newKey).apply()
                }
                false
            }
        }
    }

    private fun checkRadioGroupPressureAndSetListener() {
        sharedPreferences?.run {
            settings_radGrpPressure.check(
                getInt(
                    PRESSURE_KEY,
                    R.id.settings_radBtnMmHg
                )
            )
            settings_radGrpPressure.setOnCheckedChangeListener { _, checkedId ->
                edit().putInt(PRESSURE_KEY, checkedId).apply()
            }
        }
    }

    private fun checkRadioGroupDegreeAndSetListener() {
        sharedPreferences?.run {
            settings_radGrpDegree.check(
                getInt(
                    DEGREE_KEY,
                    R.id.settings_radBtnCelsius
                )
            )
            settings_radGrpDegree.setOnCheckedChangeListener { _, checkedId ->
                edit().putInt(DEGREE_KEY, checkedId).apply()
            }
        }
    }

}