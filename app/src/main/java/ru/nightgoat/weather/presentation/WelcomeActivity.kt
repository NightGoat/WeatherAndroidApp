package ru.nightgoat.weather.presentation

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import ru.nightgoat.weather.R

class WelcomeActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        addSlide(Fragment(R.layout.fragment_intro_1))
        addSlide(Fragment(R.layout.fragment_intro_2))
        addSlide(Fragment(R.layout.fragment_intro_3))
        isSkipButtonEnabled = true
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }
}