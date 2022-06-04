package com.tardigrade.capstonebangkit.data.repository

import com.tardigrade.capstonebangkit.data.api.ApiService
import com.tardigrade.capstonebangkit.data.preference.SessionPreferences
import com.tardigrade.capstonebangkit.utils.toRequestBody
import org.json.JSONObject

class AuthRepository(
    private val apiService: ApiService,
    private val pref: SessionPreferences
) {
    suspend fun register(name: String, email: String, password: String) {
        val requestJson = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }

        apiService.register(requestJson.toRequestBody())
    }

    suspend fun login(email: String, password: String): Boolean {
        val requestJson = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val response = apiService.login(requestJson.toRequestBody())

        pref.setSession(response.token, response.hasPin)

        return response.hasPin
    }
}