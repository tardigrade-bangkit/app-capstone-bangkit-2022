package com.tardigrade.capstonebangkit.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

fun JSONObject.toRequestBody() = this.toString()
    .toRequestBody("application/json".toMediaTypeOrNull())