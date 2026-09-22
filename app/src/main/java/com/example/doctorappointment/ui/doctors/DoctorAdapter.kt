package com.example.doctorappointment.ui.doctors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointment.data.Doctor
import com.example.doctorappointment.databinding.ItemDoctorBinding

/**
 * Адаптер для отображения списка врачей.
 */
class DoctorAdapter(
    private var doctors: List<Doctor>,
    private val onClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(val binding: ItemDoctorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemDoctorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.binding.tvName.text = doctor.name
        holder.binding.tvSpecialty.text = doctor.specialty
        holder.binding.tvPhone.text = doctor.phone

        holder.itemView.setOnClickListener { onClick(doctor) }
    }

    override fun getItemCount() = doctors.size

    fun updateData(newDoctors: List<Doctor>) {
        doctors = newDoctors
        notifyDataSetChanged()
    }
}