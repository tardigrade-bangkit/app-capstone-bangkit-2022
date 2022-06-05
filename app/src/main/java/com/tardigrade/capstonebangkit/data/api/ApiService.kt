package com.tardigrade.capstonebangkit.data.api

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun register(
        @Body requestBody: RequestBody
    ): GenericResponse

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(
        @Body requestBody: RequestBody
    ): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("pin")
    suspend fun addPin(
        @Header("x-access-token") token: String,
        @Body requestBody: RequestBody
    ) : GenericResponse

    @Headers("Content-Type: application/json")
    @POST("pin/check")
    suspend fun checkPin(
        @Header("x-access-token") token: String,
        @Body requestBody: RequestBody
    ) : GenericResponse
}