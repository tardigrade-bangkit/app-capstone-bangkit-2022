package com.tardigrade.capstonebangkit.data.api

import com.google.gson.annotations.SerializedName

data class AddChild(
    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("birthdate")
    val birthdate: String = "",

    @field:SerializedName("avatar")
    val avatar_id: Int,
)