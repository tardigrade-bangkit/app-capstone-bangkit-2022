package com.tardigrade.capstonebangkit.data.api

import com.google.gson.annotations.SerializedName
import com.tardigrade.capstonebangkit.data.model.Answer

data class AddChild(
    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("birthdate")
    val birthdate: String = "",

    @field:SerializedName("avatar")
    val avatar_id: Int,
)

data class NewUser(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class PinData(
    @field:SerializedName("pin")
    val pin: String
)

data class LoginData(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class AddUsageData(
    @field:SerializedName("time_start")
    val time_start: String
)

data class AddEndUsageData(
    @field:SerializedName("time_end")
    val time_end: String
)

data class PostAnswerBody(
    @field:SerializedName("list_answer")
    val listAnswer: List<Answer>
)