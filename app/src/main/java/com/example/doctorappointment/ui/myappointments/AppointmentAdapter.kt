package com.example.doctorappointment.ui.myappointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointment.data.Appointment
import com.example.doctorappointment.databinding.ItemAppointmentBinding

/**
 * Адаптер для списка записей к врачам.
 * @param onCancel нажатие на кнопку "Отменить запись"
 */
class AppointmentAdapter(
    private var appointments: List<Appointment>,
    private val onCancel: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = appointments[position]
        holder.binding.tvDoctorName.text = item.doctorName
        holder.binding.tvSpecialty.text = item.specialty
        holder.binding.tvDateTime.text = "${item.date} в ${item.time}"

        holder.binding.btnCancel.setOnClickListener {
            onCancel(item)
        }
    }

    override fun getItemCount() = appointments.size

    fun updateData(newList: List<Appointment>) {
        appointments = newList
        notifyDataSetChanged()
    }
}