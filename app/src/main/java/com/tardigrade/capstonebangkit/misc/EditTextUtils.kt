package com.tardigrade.capstonebangkit.misc

import android.content.Context
import android.widget.EditText
import com.tardigrade.capstonebangkit.R

fun EditText.validate(
    context: Context?,
    name: String,
    validation: ((EditText) -> Boolean)? = null
): Boolean {
    val inputted = text.toString()
    if (inputted.isEmpty()) {
        error = context?.getString(R.string.input_empty, name)
        return false
    }

    return validation?.invoke(this) ?: true
}