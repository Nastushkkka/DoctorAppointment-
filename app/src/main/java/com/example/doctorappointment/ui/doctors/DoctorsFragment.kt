package com.example.doctorappointment.ui.doctors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointment.databinding.FragmentDoctorsBinding

/**
 * Экран со списком врачей.
 * Загружает данные из API и показывает в RecyclerView.
 */
class DoctorsFragment : Fragment() {

    private var _binding: FragmentDoctorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DoctorViewModel
    private lateinit var adapter: DoctorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DoctorViewModel::class.java]

        // Настраиваем RecyclerView
        binding.rvDoctors.layoutManager = LinearLayoutManager(requireContext())
        adapter = DoctorAdapter(emptyList()) { doctor ->
            Toast.makeText(requireContext(), "Выбран: ${doctor.name}", Toast.LENGTH_SHORT).show()
            // TODO: переход на экран записи
        }
        binding.rvDoctors.adapter = adapter

        // Наблюдаем за списком врачей
        viewModel.doctors.observe(viewLifecycleOwner) { doctors ->
            binding.progressBar.visibility = View.GONE
            adapter.updateData(doctors)
        }

        // Наблюдаем за ошибками
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = error
                viewModel.clearError()
            } else {
                binding.tvError.visibility = View.GONE
            }
        }

        // Загружаем врачей
        binding.progressBar.visibility = View.VISIBLE
        viewModel.loadDoctors()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}