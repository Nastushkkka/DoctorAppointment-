package com.example.doctorappointment.ui.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.doctorappointment.data.Appointment
import com.example.doctorappointment.databinding.FragmentAppointmentBinding
import com.example.doctorappointment.ui.doctors.DoctorViewModel

class AppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DoctorViewModel

    // Данные о выбранном враче (передаются через Bundle)
    private var doctorId: Int = -1
    private var doctorName: String = "Не выбран"
    private var specialty: String = "—"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Получаем данные врача из аргументов
        arguments?.let {
            doctorId = it.getInt("doctorId", -1)
            doctorName = it.getString("doctorName", "Не выбран") ?: "Не выбран"
            specialty = it.getString("specialty", "—") ?: "—"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DoctorViewModel::class.java]

        // Показываем данные врача
        binding.tvDoctorName.text = doctorName
        binding.tvSpecialty.text = specialty

        // Обработчик кнопки
        binding.btnSave.setOnClickListener {
            saveAppointment()
        }
    }

    private fun saveAppointment() {
        val date = binding.etDate.text.toString().trim()
        val time = binding.etTime.text.toString().trim()

        // ========== ПРОВЕРКА: врач выбран ==========
        if (doctorId == -1) {
            Toast.makeText(requireContext(), "Сначала выберите врача", Toast.LENGTH_SHORT).show()
            return
        }

        // ========== ПРОВЕРКА: поля не пустые ==========
        if (date.isEmpty()) {
            binding.etDate.error = "Введите дату"
            return
        }
        if (time.isEmpty()) {
            binding.etTime.error = "Введите время"
            return
        }

        // ========== ПРОВЕРКА: формат даты и времени ==========
        if (!date.matches(Regex("\\d{2}\\.\\d{2}\\.\\d{4}"))) {
            binding.etDate.error = "Формат: ДД.ММ.ГГГГ"
            return
        }
        if (!time.matches(Regex("\\d{2}:\\d{2}"))) {
            binding.etTime.error = "Формат: ЧЧ:ММ"
            return
        }

        // ========== ПРОВЕРКА: дата не в прошлом ==========
        try {
            val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
            dateFormat.isLenient = false // строгий разбор (нельзя 32.13.2025)
            val parsedDate: java.util.Date = dateFormat.parse(date) ?: throw Exception()

            // Сегодняшняя дата без времени
            val today = java.util.Calendar.getInstance()
            today.set(java.util.Calendar.HOUR_OF_DAY, 0)
            today.set(java.util.Calendar.MINUTE, 0)
            today.set(java.util.Calendar.SECOND, 0)
            today.set(java.util.Calendar.MILLISECOND, 0)

            if (parsedDate.before(today.time)) {
                binding.etDate.error = "Дата не может быть в прошлом"
                Toast.makeText(requireContext(), "Выберите будущую дату", Toast.LENGTH_SHORT).show()
                return
            }

            // Проверка: не более чем через год
            val maxDate = java.util.Calendar.getInstance()
            maxDate.add(java.util.Calendar.YEAR, 1)
            if (parsedDate.after(maxDate.time)) {
                binding.etDate.error = "Дата слишком далеко (не более года)"
                return
            }
        } catch (e: Exception) {
            binding.etDate.error = "Неверная дата"
            return
        }

        // ========== ПРОВЕРКА: корректное время (часы 0-23, минуты 0-59) ==========
        val hours = time.substring(0, 2).toIntOrNull()
        val minutes = time.substring(3, 5).toIntOrNull()

        if (hours == null || minutes == null || hours !in 0..23 || minutes !in 0..59) {
            binding.etTime.error = "Неверное время"
            return
        }

        // (опционально) Рабочие часы: 8:00 - 20:00
        if (hours < 8 || hours > 20) {
            binding.etTime.error = "Рабочие часы: с 8:00 до 20:00"
            Toast.makeText(requireContext(), "Клиника работает с 8:00 до 20:00", Toast.LENGTH_SHORT).show()
            return
        }

        // ========== СОХРАНЕНИЕ ==========
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.isEnabled = false

        val appointment = Appointment(
            doctorId = doctorId,
            doctorName = doctorName,
            specialty = specialty,
            date = date,
            time = time
        )
        viewModel.saveAppointment(appointment)

        binding.root.postDelayed({
            binding.progressBar.visibility = View.GONE
            binding.btnSave.isEnabled = true
            Toast.makeText(requireContext(), "Вы записаны к врачу!", Toast.LENGTH_LONG).show()
            binding.etDate.text.clear()
            binding.etTime.text.clear()
        }, 800)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}