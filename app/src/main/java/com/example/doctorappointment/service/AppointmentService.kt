package com.example.doctorappointment.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.doctorappointment.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Фоновый сервис для проверки записей к врачу.
 * Отправляет уведомление только ОДИН РАЗ на каждую запись.
 */
class AppointmentService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isRunning = false

    // Здесь храним ID записей, на которые уже отправили уведомление
    private val notifiedIds = mutableSetOf<Int>()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Сервис запущен")
        NotificationHelper.createChannel(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            startChecking()
        }
        return START_STICKY
    }

    private fun startChecking() {
        serviceScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val dao = db.doctorDao()
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

            while (isRunning) {
                try {
                    val today = dateFormat.format(Date())
                    val all = dao.getAllAppointments()
                    val todayAppointments = all.filter { it.date == today }

                    Log.d(TAG, "Проверка записей на $today: найдено ${todayAppointments.size}")

                    // Отправляем уведомление ТОЛЬКО для тех записей, о которых ещё не уведомляли
                    todayAppointments.forEach { appt ->
                        if (appt.id !in notifiedIds) {
                            val title = "Напоминание о приёме"
                            val text = "Сегодня в ${appt.time} у вас приём: ${appt.doctorName}"
                            NotificationHelper.showNotification(
                                applicationContext,
                                appt.id,
                                title,
                                text
                            )
                            notifiedIds.add(appt.id)
                            Log.d(TAG, "Показано уведомление для записи id=${appt.id}")
                        }
                    }

                    // Убираем из notifiedIds те записи, которые уже удалены или не на сегодня
                    val currentIds = all.map { it.id }.toSet()
                    notifiedIds.retainAll(currentIds)

                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка проверки: ${e.message}")
                }

                delay(30_000) // 30 секунд для теста (в реальности — 15 минут)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        serviceScope.cancel()
        Log.d(TAG, "Сервис остановлен")
    }

    companion object {
        private const val TAG = "AppointmentService"
    }
}