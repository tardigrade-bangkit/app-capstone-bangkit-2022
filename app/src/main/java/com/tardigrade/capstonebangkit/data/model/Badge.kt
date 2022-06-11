package com.tardigrade.capstonebangkit.data.model

import com.google.gson.annotations.SerializedName

data class Badge (
    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("acquired_date")
    val acquiredDate: String,
)