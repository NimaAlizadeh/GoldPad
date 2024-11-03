package com.example.goldpad.repositories

import com.example.goldpad.database.dao.UserDao
import com.example.goldpad.database.entities.User
import com.example.goldpad.utils.UserPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.first


class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) {

    suspend fun getTokenFromDataStore(): String? {
        return userPreferences.getToken()
    }

    suspend fun getUserByToken(token: String): User? {
        return userDao.getUserByToken(token)
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun saveUserId(userId: Int) {
        userPreferences.saveUserId(userId)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}

