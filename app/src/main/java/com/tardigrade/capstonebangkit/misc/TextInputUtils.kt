package com.tardigrade.capstonebangkit.misc

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
    Log.d("TAG", "validate: $inputted")
    if (inputted.isBlank()) {
        Log.d("TAG", "validate: empty")
        error = context?.getString(R.string.input_empty, name)
        return false
    }

    val otherErrorMessage = validation?.invoke(inputted)

    if (otherErrorMessage == null) {
        error = null
        return true
    } else {
        error = otherErrorMessage
        return false
    }
}