package com.tardigrade.capstonebangkit.data.api

import com.google.gson.annotations.SerializedName

data class GenericResponse(
	@field:SerializedName("msg")
	val msg: String
)
