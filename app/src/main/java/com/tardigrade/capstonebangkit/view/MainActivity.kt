package com.tardigrade.capstonebangkit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tardigrade.capstonebangkit.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
    }
}