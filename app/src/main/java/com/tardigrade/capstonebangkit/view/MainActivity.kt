package com.tardigrade.capstonebangkit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView = binding.navParentView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment

        navView.setupWithNavController(navHostFragment.navController)
        navView.setOnItemSelectedListener {
            if (NavigationUI.onNavDestinationSelected(it, navHostFragment.navController)) {
                true
            } else {
                if (it.itemId == R.id.nav_children_area) {
                    Toast.makeText(this, "Children area", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    false
                }
            }
        }

        navHostFragment.navController.addOnDestinationChangedListener { _, dest, _ ->
            navView.visibility = if (
                dest.id == R.id.nav_dashboard
                || dest.id == R.id.nav_profile
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}