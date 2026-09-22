package com.example.doctorappointment.data.remote

import com.example.doctorappointment.data.Doctor

/**
 * Преобразует данные из API в модель Doctor.
 * Специальности и другие поля заполняем сами, т.к. их нет в публичном API.
 */
fun ApiUser.toDoctor(): Doctor {
    val specialties = listOf(
        "Терапевт", "Стоматолог", "Окулист", "Хирург", "Кардиолог",
        "Невролог", "Педиатр", "ЛОР", "Дерматолог", "Эндокринолог"
    )
    return Doctor(
        id = this.id,
        name = this.name,
        specialty = specialties[this.id % specialties.size],
        email = this.email,
        phone = this.phone
    )
}