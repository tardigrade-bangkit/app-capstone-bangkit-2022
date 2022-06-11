package com.tardigrade.capstonebangkit.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("avatar")
    val avatar: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("has_pin")
    val hasPin: Boolean
) : Parcelable