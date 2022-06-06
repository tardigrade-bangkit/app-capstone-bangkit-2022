package com.tardigrade.capstonebangkit.data.repository

import com.tardigrade.capstonebangkit.data.api.ApiService

class ProfileRepository (private val apiService: ApiService) {
    suspend fun getAvatars() = apiService.getAvatars().avatars

    suspend fun getChildren(token: String) = apiService.getChildren(token).children

    suspend fun getSelf(token: String) = apiService.getSelf(token).user
}