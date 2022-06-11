package com.tardigrade.capstonebangkit.data.repository

import com.tardigrade.capstonebangkit.data.api.AddChild
import com.tardigrade.capstonebangkit.data.api.ApiService
import com.tardigrade.capstonebangkit.data.model.ChildProfile

class ProfileRepository(private val apiService: ApiService) {
    suspend fun getAvatars() = apiService.getAvatars().avatars

    suspend fun getChildren(token: String) = apiService.getChildren(token).children

    suspend fun getSelf(token: String) = apiService.getSelf(token).user

    suspend fun addChildren(token: String, child: AddChild) {
        apiService.addChildren(token, child)
    }

    suspend fun getChildrenProgress(token: String, child: ChildProfile) =
        apiService.getChildrenLesson(token, child.id).lessons

    suspend fun getChildrenBadges(token: String, child: ChildProfile) =
        apiService.getChildrenBadges(token, child.id).badges

    suspend fun getChildrenAchievements(token: String, child: ChildProfile) =
        apiService.getChildrenAchievements(token, child.id).achievements
}