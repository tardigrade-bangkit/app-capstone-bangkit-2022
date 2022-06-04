package com.tardigrade.capstonebangkit.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun register(
        @Body requestBody: RequestBody
    ): GenericResponse
//
//    @FormUrlEncoded
//    @POST("login")
//    suspend fun login(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): LoginRespons
}