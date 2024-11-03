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

    fun checkUserToken() {
        viewModelScope.launch {
            val token = userRepository.getTokenFromDataStore()
            if (token != null) {
                val user = withContext(Dispatchers.IO) {
                    userRepository.getUserByToken(token)
                }
                if (user != null) {
                    user.id?.let {
                        userRepository.saveUserId(it)
                        Constants.USER_ID = it
                    }
                    navigateToPersonalRequest.postValue(true)
                } else {
                    navigateToLogin.postValue(true)
                    return@launch
                }
            } else {
                navigateToLogin.postValue(true)
                return@launch
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