package com.tardigrade.capstonebangkit.utils

import android.content.Context
import android.util.Log
import com.google.android.material.textfield.TextInputLayout
import com.tardigrade.capstonebangkit.R

fun TextInputLayout.validate(
    context: Context?,
    name: String,
    validation: ((String) -> String?)? = null
): Boolean {
    val inputted = editText?.text.toString()
    if (inputted.isBlank()) {
        error = context?.getString(R.string.input_empty, name)
        return false
    }

    error = validation?.invoke(inputted)

    return error == null
}