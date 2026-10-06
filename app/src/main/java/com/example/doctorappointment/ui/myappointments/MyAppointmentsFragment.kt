package com.example.doctorappointment.ui.myappointments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointment.data.Appointment
import com.example.doctorappointment.databinding.FragmentMyAppointmentsBinding
import com.example.doctorappointment.ui.doctors.DoctorViewModel

class MyAppointmentsFragment : Fragment() {

    private var _binding: FragmentMyAppointmentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DoctorViewModel
    private lateinit var adapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DoctorViewModel::class.java]

        // Настраиваем RecyclerView
        binding.rvAppointments.layoutManager = LinearLayoutManager(requireContext())
        adapter = AppointmentAdapter(emptyList()) { appointment ->
            confirmCancel(appointment)
        }
        binding.rvAppointments.adapter = adapter

        // Наблюдаем за списком записей
        viewModel.appointments.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvAppointments.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvAppointments.visibility = View.VISIBLE
                adapter.updateData(list)
            }
        }

        // Загружаем записи из Room
        viewModel.loadAppointments()
    }

    private fun confirmCancel(appointment: Appointment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Отмена записи")
            .setMessage("Отменить запись к ${appointment.doctorName} на ${appointment.date} в ${appointment.time}?")
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteAppointment(appointment.id)
                Toast.makeText(requireContext(), "Запись отменена", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}