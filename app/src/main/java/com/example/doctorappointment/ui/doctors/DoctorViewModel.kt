package com.example.doctorappointment.ui.doctors

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.doctorappointment.data.Appointment
import com.example.doctorappointment.data.Doctor
import com.example.doctorappointment.data.local.AppDatabase
import com.example.doctorappointment.data.remote.RetrofitClient
import com.example.doctorappointment.data.remote.toDoctor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel для управления врачами и записями.
 * Здесь: загрузка с API, кеширование в Room, чтение из Room.
 */
class DoctorViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).doctorDao()
    private val api = RetrofitClient.apiService

    // LiveData для списка врачей
    private val _doctors = MutableLiveData<List<Doctor>>()
    val doctors: LiveData<List<Doctor>> = _doctors

    // LiveData для записей
    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    // LiveData для ошибок
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Загрузка врачей с публичного API и сохранение в Room.
     * Если интернета нет — берёт данные из Room (кеша).
     */
    fun loadDoctors() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("DoctorViewModel", "Загрузка врачей с API...")
                val apiUsers = api.getUsers()
                val doctors = apiUsers.map { it.toDoctor() }

                // Сохраняем в Room (кеш)
                dao.insertDoctors(doctors)
                Log.d("DoctorViewModel", "Загружено врачей: ${doctors.size}")

                // Публикуем в LiveData
                _doctors.postValue(doctors)
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Ошибка загрузки: ${e.message}")
                _error.postValue("Ошибка загрузки. Загружаем из кеша...")

                // Если ошибка — берём из Room
                val cached = dao.getAllDoctors()
                _doctors.postValue(cached)
            }
        }
    }

    /**
     * Загрузка врачей из локальной БД (кеша).
     */
    fun loadDoctorsFromCache() {
        viewModelScope.launch(Dispatchers.IO) {
            val cached = dao.getAllDoctors()
            _doctors.postValue(cached)
        }
    }

    /**
     * Сохранение записи к врачу.
     */
    fun saveAppointment(appointment: Appointment) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.insertAppointment(appointment)
                Log.d("DoctorViewModel", "Запись сохранена: ${appointment.doctorName}")
                loadAppointments() // обновляем список записей
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Ошибка сохранения: ${e.message}")
                _error.postValue("Не удалось сохранить запись")
            }
        }
    }

    /**
     * Загрузка всех записей из Room.
     */
    fun loadAppointments() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = dao.getAllAppointments()
            _appointments.postValue(list)
        }
    }

    /**
     * Удаление записи.
     */
    fun deleteAppointment(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAppointment(id)
            Log.d("DoctorViewModel", "Запись удалена: id=$id")
            loadAppointments()
        }
    }

    fun clearError() {
        _error.value = null
    }
}