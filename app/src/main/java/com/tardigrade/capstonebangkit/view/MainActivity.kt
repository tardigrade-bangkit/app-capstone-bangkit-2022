package com.tardigrade.capstonebangkit.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.ActivityMainBinding

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customToolbar)
        binding.customToolbar.apply {
            setTitleTextAppearance(this@MainActivity, R.style.Text_Heading_1)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }

        val navView = binding.navParentView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment

        setupActionBarWithNavController(
            navHostFragment.navController, AppBarConfiguration(
                setOf(
                    R.id.nav_dashboard, R.id.nav_profile, R.id.childCreatedFragment
                )
            )
        )
        navView.setupWithNavController(navHostFragment.navController)
        navView.setOnItemSelectedListener {
            if (it.itemId == R.id.nav_children_area) {
                Toast.makeText(this, "Children area", Toast.LENGTH_SHORT).show()
            }

            NavigationUI.onNavDestinationSelected(it, navHostFragment.navController)
        }

        val childrenButton = (navView.getChildAt(0) as BottomNavigationMenuView)
            .getChildAt(1) as BottomNavigationItemView
        childrenButton.background = ContextCompat.getDrawable(this, R.drawable.nav_bottom_item_bg_children)
        childrenButton.setOnClickListener {
            navHostFragment.navController.navigate(R.id.chooseChildFragment)
        }

        // these two lines show up as an error but they actually works as expected
        // hence the SuppressLint
        childrenButton.setTextColor(ColorStateList.valueOf(Color.WHITE))
        childrenButton.setIconTintList(ColorStateList.valueOf(Color.WHITE))

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