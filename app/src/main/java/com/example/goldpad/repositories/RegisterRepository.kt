package com.example.goldpad.repositories


import com.example.goldpad.database.dao.UserDao
import com.example.goldpad.database.entities.User
import com.example.goldpad.utils.UserPreferences
import com.example.goldpad.utils.generateUserToken
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) {
    suspend fun registerUser(username: String, password: String, firstName: String, lastName: String, bankName: String, userId: String, shebaCode: String): User {
        val user = User(
            username = username,
            firstName = firstName,
            lastName = lastName,
            password = password,
            bankName = bankName,
            userId = userId,
            shebaCode = shebaCode,
            token = generateUserToken()
        )
        val userId = userDao.insertUser(user)
        user.id = userId.toInt()
        userPreferences.saveToken(user.token!!)
        userPreferences.saveUserId(user.id)
        return user
    }
}