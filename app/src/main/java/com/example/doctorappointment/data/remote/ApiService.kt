package com.example.doctorappointment.data.remote

import retrofit2.http.GET

/**
 * Интерфейс для работы с публичным API.
 * Retrofit сам сгенерирует реализацию.
 */
interface ApiService {

    /**
     * Получает список пользователей с публичного API.
     * URL: https://jsonplaceholder.typicode.com/users
     */
    @GET("users")
    suspend fun getUsers(): List<ApiUser>
}