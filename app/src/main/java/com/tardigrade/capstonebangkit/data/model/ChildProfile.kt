package com.tardigrade.capstonebangkit.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChildProfile(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("avatar")
    val avatarUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("level")
    val level: Int = 0,

    @field:SerializedName("birthdate")
    val birthdate: String,
) : Parcelable
