package com.example.goldpad.repositories


import com.example.goldpad.database.dao.UserDao
import com.example.goldpad.database.entities.User
import com.example.goldpad.utils.UserPreferences
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) {
    suspend fun login(username: String, password: String): User? {
        return userDao.getUserByUsernameAndPassword(username, password)
    }

    suspend fun saveToken(token: String) {
        userPreferences.saveToken(token)
    }

    suspend fun saveUserId(userId: Int) {
        userPreferences.saveUserId(userId)
    }

    suspend fun getToken(): String? {
        return userPreferences.getToken()
    }

    suspend fun updateTokenForUser(userId: Int, token: String?) {
        userDao.updateUserToken(userId, token)
    }
}
