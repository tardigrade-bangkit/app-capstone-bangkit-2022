package com.tardigrade.capstonebangkit.data.api

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun register(
        @Body requestBody: RequestBody
    ): GenericResponse

    @POST("login")
    suspend fun login(
        @Body requestBody: RequestBody
    ): LoginResponse
}