package com.tardigrade.capstonebangkit.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Usage(
    @field:SerializedName("time_start")
    val timeStart: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("time_end")
    val timeEnd: String? = null
)

data class DailyUsage(
    val date: Date,
    var duration: Long = 0
)