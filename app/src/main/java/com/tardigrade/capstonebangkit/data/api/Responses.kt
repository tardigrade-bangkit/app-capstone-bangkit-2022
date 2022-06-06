package com.tardigrade.capstonebangkit.data.api

import com.google.gson.annotations.SerializedName
import com.tardigrade.capstonebangkit.data.model.Avatar
import com.tardigrade.capstonebangkit.data.model.ChildProfile

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
