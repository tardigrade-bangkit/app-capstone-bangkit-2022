package com.tardigrade.capstonebangkit.data.api

import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun register(
        @Body newUser: NewUser
    ): GenericResponse

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(
        @Body loginData: LoginData
    ): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("pin")
    suspend fun addPin(
        @Header("x-access-token") token: String,
        @Body pinData: PinData
    ) : GenericResponse

    @Headers("Content-Type: application/json")
    @POST("pin/check")
    suspend fun checkPin(
        @Header("x-access-token") token: String,
        @Body pinData: PinData
    ) : GenericResponse

    @GET("avatars")
    suspend fun getAvatars() : GetAvatarsResponse

    @GET("children")
    suspend fun getChildren(
        @Header("x-access-token") token: String
    )  : GetChildrenResponse

    @GET("users/self")
    suspend fun getSelf(
        @Header("x-access-token") token: String
    ) : GetSelfResponse

    @Headers("Content-Type: application/json")
    @POST("children")
    suspend fun addChildren(
        @Header("x-access-token") token: String,
        @Body newChild: AddChild
    ): GenericResponse
}