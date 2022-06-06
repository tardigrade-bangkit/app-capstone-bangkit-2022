package com.tardigrade.capstonebangkit.data.repository

import com.tardigrade.capstonebangkit.data.api.ApiService
import com.tardigrade.capstonebangkit.data.api.LoginData
import com.tardigrade.capstonebangkit.data.api.NewUser
import com.tardigrade.capstonebangkit.data.api.PinData
import com.tardigrade.capstonebangkit.data.preference.SessionPreferences
import com.tardigrade.capstonebangkit.utils.toRequestBody
import org.json.JSONObject

class AuthRepository(
    private val apiService: ApiService,
    private val pref: SessionPreferences
) {
    suspend fun register(newUser: NewUser) {
        apiService.register(newUser)
    }

    suspend fun login(loginData: LoginData): Boolean {
        val response = apiService.login(loginData)

        pref.setSession(response.token, response.hasPin)

        return response.hasPin
    }

    suspend fun addPin(token: String, pin: PinData) {
        apiService.addPin(token, pin)

        pref.setHasPin(true)
    }

    suspend fun checkPin(token: String, pin: PinData) {
        apiService.checkPin(token, pin)
    }
}