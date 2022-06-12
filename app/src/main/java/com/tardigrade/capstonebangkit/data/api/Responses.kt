package com.tardigrade.capstonebangkit.data.api

import com.google.gson.annotations.SerializedName
import com.tardigrade.capstonebangkit.data.model.*

data class GenericResponse(
	@field:SerializedName("msg")
	val msg: String
)

data class LoginResponse(
	@field:SerializedName("has_pin")
	val hasPin: Boolean,

	@field:SerializedName("token")
	val token: String
)

data class GetChildrenResponse(
	@field:SerializedName("children")
	val children: List<ChildProfile>
)

data class GetAvatarsResponse(
	@field:SerializedName("avatars")
	val avatars: List<Avatar>
)

data class GetSelfResponse(
	@field:SerializedName("user")
	val user: User
)

data class GetProgressResponse(
	@field:SerializedName("lessons")
	val lessons: List<Lesson>
)

data class GetBadgesResponse(
	@field:SerializedName("badges")
	val badges: List<Badge>
)

data class GetAchievementsResponse(
	@field:SerializedName("achievements")
	val achievements: List<Achievement>
)

data class GetUsagesResponse(
	@field:SerializedName("usages")
	val usages: List<Usage>
)
