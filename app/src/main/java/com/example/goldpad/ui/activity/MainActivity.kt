package com.example.goldpad.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.goldpad.R
import com.example.goldpad.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        navController = findNavController(R.id.main_navigation_host)
        binding.mainBottomNavigation.setupWithNavController(navController)


        binding.apply {
            navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
                when (destination.id) {
                    R.id.notificationsFragment, R.id.personalRequestsFragment -> {
                        binding.mainBottomNavigation.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.mainBottomNavigation.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }
}