package com.example.doctorappointment.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.doctorappointment.data.Appointment
import com.example.doctorappointment.data.Doctor

/**
 * Основной класс базы данных Room.
 * Singleton — создаётся один раз на всё приложение.
 */
@Database(
    entities = [Doctor::class, Appointment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun doctorDao(): DoctorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "doctor_appointment_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}