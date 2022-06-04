package com.tardigrade.capstonebangkit.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

fun getActionBar(activity: FragmentActivity?) =
    (activity as AppCompatActivity).supportActionBar