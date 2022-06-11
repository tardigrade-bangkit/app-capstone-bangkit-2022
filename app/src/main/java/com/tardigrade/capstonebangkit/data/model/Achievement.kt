package com.tardigrade.capstonebangkit.data.model

import com.google.gson.annotations.SerializedName

data class Achievement(
    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("acquired_date")
    val acquiredDate: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: Int
)
