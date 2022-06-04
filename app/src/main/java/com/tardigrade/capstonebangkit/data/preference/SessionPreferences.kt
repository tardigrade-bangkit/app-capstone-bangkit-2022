package com.tardigrade.capstonebangkit.data.preference

import android.content.Context
import androidx.core.content.edit

class SessionPreferences (private val context: Context) {
    private val preferences = context
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setSession(token: String, hasPin: Boolean) {
        preferences.edit {
            putString(TOKEN, token)
            putBoolean(HAS_PIN, hasPin)
        }
    }

    fun getToken() = preferences.getString(TOKEN, null)

    fun hasPin() = if (!preferences.contains(HAS_PIN)) {
        null
    } else {
        preferences.getBoolean(HAS_PIN, false)
    }

    companion object {
        private const val PREFS_NAME = "session_pref"
        private const val TOKEN = "token"
        private const val HAS_PIN = "has_pin"
    }
}