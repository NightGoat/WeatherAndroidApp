package ru.nightgoat.weather.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.utils.FIRST_TIME_OPEN_KEY
import ru.nightgoat.weather.core.utils.SETTINGS_KEY

class MainActivity : DaggerAppCompatActivity() {

    private var preferences: SharedPreferences? = null
    private var navController: NavController? = null
    private var navView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment).also { controller ->
            navView?.setupWithNavController(controller)
        }
        setUpPreferences()
        reactToFirstTimeOpen()
    }

    private fun setUpPreferences() {
        preferences = getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
    }

    private fun reactToFirstTimeOpen() {
        val isWasAlreadyOpened = preferences?.getBoolean(FIRST_TIME_OPEN_KEY, false)
        if (isWasAlreadyOpened == false) {
            preferences?.edit()?.run {
                putBoolean(FIRST_TIME_OPEN_KEY, true)
                apply()
            }
            openWelcomeActivity()
        }
    }

    private fun openWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    fun navigateToList() {
        navView?.selectedItemId = R.id.navigation_list
    }
}
