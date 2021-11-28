package ru.nightgoat.weather.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import ru.nightgoat.weather.R
import ru.nightgoat.weather.core.utils.FIRST_TIME_OPEN_KEY
import ru.nightgoat.weather.core.utils.SETTINGS_KEY

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        val sharedPreferences = getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
        val isWasAlreadyOpened = sharedPreferences.getBoolean(FIRST_TIME_OPEN_KEY, false)
        reactToFirstTimeOpen(isWasAlreadyOpened, sharedPreferences)
    }

    private fun reactToFirstTimeOpen(
        isWasAlreadyOpened: Boolean,
        sharedPreferences: SharedPreferences
    ) {
        if (!isWasAlreadyOpened) {
            sharedPreferences.edit().run {
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
}
