package com.example.doctorappointment.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.doctorappointment.data.Appointment
import com.example.doctorappointment.data.Doctor

/**
 * DAO — интерфейс для работы с базой данных.
 * Room автоматически генерирует реализацию.
 */
@Dao
interface DoctorDao {

    // ==================== ВРАЧИ ====================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<Doctor>)

    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctors(): List<Doctor>

    // ==================== ЗАПИСИ ====================
    @Insert
    suspend fun insertAppointment(appointment: Appointment)

    @Query("SELECT * FROM appointments ORDER BY id DESC")
    suspend fun getAllAppointments(): List<Appointment>

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteAppointment(id: Int)

    @Query("SELECT * FROM appointments WHERE status = 'Активна'")
    suspend fun getActiveAppointments(): List<Appointment>
}