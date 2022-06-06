package com.tardigrade.capstonebangkit.data.model

import com.google.gson.annotations.SerializedName

data class Avatar(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("image_url")
    val url: String,
)
