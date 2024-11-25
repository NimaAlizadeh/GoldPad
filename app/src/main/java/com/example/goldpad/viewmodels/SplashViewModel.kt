package com.example.goldpad.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldpad.database.entities.User
import com.example.goldpad.repositories.UserRepository
import com.example.goldpad.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val navigateToLogin = MutableLiveData<Boolean>()
    val navigateToPersonalRequest = MutableLiveData<Boolean>()
    val navigateToAdminPage = MutableLiveData<Boolean>()



    fun checkUserToken() {
        viewModelScope.launch {
            val token = userRepository.getTokenFromDataStore()
            if (token != null) {
                val user = withContext(Dispatchers.IO) {
                    userRepository.getUserByToken(token)
                }
                if (user != null) {
                    user.id?.let {
                        userRepository.saveUserId(it) // Save user ID in repository
                        Constants.USER_ID = it       // Save user ID in constants
                    }
                    if (user.isAdmin) { // Check if the user is an admin
                        navigateToAdminPage.postValue(true) // Navigate to the admin page
                    } else {
                        navigateToPersonalRequest.postValue(true) // Navigate to the personal request page
                    }
                } else {
                    navigateToLogin.postValue(true) // Navigate to login if user is null
                }
            } else {
                navigateToLogin.postValue(true) // Navigate to login if token is null
            }
        }
    }



    fun checkAndInsertAdminUser() {
        viewModelScope.launch {
            val adminUser = userRepository.getUserByUsername("admin")
            if (adminUser == null) {
                val newAdminUser = User(
                    username = "admin",
                    password = "admin",
                    isAdmin = true
                )
                userRepository.insertUser(newAdminUser)
            }
        }
    }
}