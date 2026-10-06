package com.example.doctorappointment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.doctorappointment.databinding.ActivityMainBinding
import com.example.doctorappointment.ui.author.AuthorFragment
import com.example.doctorappointment.ui.doctors.DoctorsFragment
import com.example.doctorappointment.ui.feedback.FeedbackFragment
import com.example.doctorappointment.ui.myappointments.MyAppointmentsFragment
import com.example.doctorappointment.ui.splash.SplashFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Загружаем первый экран — Splash
        if (savedInstanceState == null) {
            loadFragment(SplashFragment())
        }

        // Обработка клика по меню
        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_doctors -> DoctorsFragment()
                R.id.nav_my -> MyAppointmentsFragment()
                R.id.nav_about -> AuthorFragment()
                R.id.nav_feedback -> FeedbackFragment()
                else -> DoctorsFragment()
            }
            loadFragment(fragment)
            true
        }

        // Слушатель жизненного цикла фрагментов
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    // Скрываем меню, если открыт Splash
                    val isSplash = f is SplashFragment
                    binding.bottomNav.visibility = if (isSplash) View.GONE else View.VISIBLE
                }
            },
            true
        )
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}