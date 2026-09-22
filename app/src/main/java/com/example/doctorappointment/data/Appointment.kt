package com.example.doctorappointment.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Модель записи к врачу. Хранится в Room.
 */
@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val doctorId: Int,
    val doctorName: String,
    val specialty: String,
    val date: String,        // например, "2026-09-25"
    val time: String,        // например, "14:30"
    val status: String = "Активна"
)