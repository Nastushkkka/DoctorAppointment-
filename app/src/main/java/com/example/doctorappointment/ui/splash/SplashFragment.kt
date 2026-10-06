package com.example.doctorappointment.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.doctorappointment.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            // Показываем ProgressBar
            binding.progressBar.visibility = View.VISIBLE
            binding.btnStart.isEnabled = false

            // Через 1.5 сек переходим на экран врачей
            Handler(Looper.getMainLooper()).postDelayed({
                // Здесь просто заменяем фрагмент на список врачей
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(com.example.doctorappointment.R.id.fragmentContainer,
                        com.example.doctorappointment.ui.doctors.DoctorsFragment())
                    .commit()
            }, 1500)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}