package com.example.goldpad.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.goldpad.R
import com.example.goldpad.database.dao.UserDao
import com.example.goldpad.databinding.ActivityMainBinding
import com.example.goldpad.utils.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var userDao: UserDao

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

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                onLogoutClicked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onLogoutClicked() {
        // Show a confirmation dialog to the user
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("آیا از خروج از حساب کاربری اطمینان دارید؟")
            .setCancelable(false)
            .setPositiveButton("خروج") { _, _ ->
                performLogout()
            }
            .setNegativeButton("بیخیال") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("خروج از حساب کاربری")
        alert.show()
    }

    private fun performLogout() {
        lifecycleScope.launch {
            // Clear token from DataStore
            val userPreferences = UserPreferences(applicationContext)
            userPreferences.clearToken()

            // Clear token from database (assuming UserDao is injected and available)
            userDao?.let { dao ->
                val token = userPreferences.getToken()
                if (token != null) {
                    val user = dao.getUserByToken(token)
                    user?.let {
                        dao.updateUser(it.copy(token = ""))
                    }
                }
            }

            // Navigate to the SplashFragment
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.main_navigation_host, true)
                .build()
            navController.navigate(R.id.splashFragment, null, navOptions)
        }
    }





    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }
}