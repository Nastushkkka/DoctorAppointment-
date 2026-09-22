package com.example.doctorappointment.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Модель врача. Используется и для Room (SQLite), и для Retrofit (API).
 */
@Entity(tableName = "doctors")
data class Doctor(
    @PrimaryKey val id: Int,
    val name: String,
    val specialty: String,
    val email: String = "",
    val phone: String = ""
)