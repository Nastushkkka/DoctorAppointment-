package com.example.doctorappointment.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Модель пользователя из публичного API jsonplaceholder.typicode.com.
 * Мы будем использовать эти данные как "врачей".
 */
data class ApiUser(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("company") val company: Company?
)

data class Company(
    @SerializedName("name") val name: String
)