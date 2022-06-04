package com.tardigrade.capstonebangkit.utils

import com.google.gson.Gson
import com.tardigrade.capstonebangkit.data.api.GenericResponse
import okhttp3.ResponseBody

fun getErrorResponse(body: ResponseBody): GenericResponse = Gson().fromJson(
    body.charStream(),
    GenericResponse::class.java
)