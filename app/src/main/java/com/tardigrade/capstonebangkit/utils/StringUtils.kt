package com.tardigrade.capstonebangkit.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.toDateTime() = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault())
    .parse(this)