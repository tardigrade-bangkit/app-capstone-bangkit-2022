package com.tardigrade.capstonebangkit.data.repository

import com.tardigrade.capstonebangkit.data.api.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AuthRepository(
    private val apiService: ApiService
) {
    suspend fun register(name: String, email: String, password: String) {
        val requestJson = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }

        val requestBody = requestJson.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        apiService.register(requestBody)
    }

}