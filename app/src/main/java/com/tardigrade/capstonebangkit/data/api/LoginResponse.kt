package com.tardigrade.capstonebangkit.data.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("has_pin")
	val hasPin: Boolean,

	@field:SerializedName("token")
	val token: String
)
