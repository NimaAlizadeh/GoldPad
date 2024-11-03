package com.example.goldpad.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.goldpad.database.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user WHERE userId = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM user WHERE token = :token")
    suspend fun getUserByToken(token: String): User?

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("UPDATE user SET token = :token WHERE id = :id")
    suspend fun updateUserToken(id: Int, token: String?)

    @Query("UPDATE user SET token = NULL WHERE id = :id")
    suspend fun clearUserToken(id: Int)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User?

    @Query("UPDATE user SET token = NULL WHERE id = :id")
    suspend fun logoutUser(id: Int)
}
