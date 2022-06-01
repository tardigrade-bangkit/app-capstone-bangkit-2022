package com.tardigrade.capstonebangkit.data.model

data class ChildProfile(
    val avatarUrl: String,
    val name: String,
    val level: Int = 0,
    val tookTest: Boolean = false
)
