package ru.nightgoat.weather.presentation

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import ru.nightgoat.weather.R
import ru.nightgoat.weather.widget.GoogleLikeWidgetProvider

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("first", false)) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("first", true)
            editor.apply()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }
}
