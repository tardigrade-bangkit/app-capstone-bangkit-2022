package com.tardigrade.capstonebangkit.data.repository

import com.tardigrade.capstonebangkit.data.api.ApiService

class ChildrenDataRepository (private val apiService: ApiService) {
    suspend fun getAvatars() = apiService.getAvatars().avatars
}