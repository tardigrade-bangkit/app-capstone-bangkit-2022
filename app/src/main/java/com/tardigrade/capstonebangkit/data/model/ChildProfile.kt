package com.tardigrade.capstonebangkit.data.model

import com.google.gson.annotations.SerializedName

data class ChildProfile(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("avatar")
    val avatarUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("level")
    val level: Int = 0
)
