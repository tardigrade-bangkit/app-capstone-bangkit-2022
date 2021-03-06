package com.tardigrade.capstonebangkit.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .show()
}

fun showSnackbar(view: View, message: String, actionLabel: String, action: View.OnClickListener) {
    Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionLabel, action)
        .show()
}