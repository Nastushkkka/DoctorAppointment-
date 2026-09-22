package com.example.doctorappointment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.doctorappointment.databinding.ActivityMainBinding
import com.example.doctorappointment.ui.author.AuthorFragment
import com.example.doctorappointment.ui.doctors.DoctorsFragment
import com.example.doctorappointment.ui.myappointments.MyAppointmentsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Загружаем первый экран
        if (savedInstanceState == null) {
            loadFragment(DoctorsFragment())
        }

        // Обработка клика по меню
        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_doctors -> DoctorsFragment()
                R.id.nav_my -> MyAppointmentsFragment()
                R.id.nav_about -> AuthorFragment()
                else -> DoctorsFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}