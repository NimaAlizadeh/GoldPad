package com.example.goldpad.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.goldpad.database.dto.RequestWithUser
import com.example.goldpad.database.entities.Request
import com.example.goldpad.database.entities.User

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: Request)

    @Delete
    suspend fun deleteRequest(request: Request)

    @Update
    suspend fun updateRequest(request: Request)

    @Query("SELECT * FROM request WHERE userId = :userId")
    suspend fun getRequestsByUserId(userId: Int): List<Request>

    @Query("SELECT * FROM request")
    fun getAllRequests(): List<Request>

    @Transaction
    @Query("SELECT * FROM request")
    suspend fun getAllRequestsWithUsers(): List<RequestWithUser>

    @Query("SELECT * FROM request WHERE isActive = 1 AND mode = :isBuyer")
    suspend fun getActiveRequests(isBuyer: Boolean): List<Request>

    @Query("UPDATE request SET isActive = 0 WHERE id IN (:requestIds)")
    suspend fun deactivateRequests(requestIds: List<Int>)
}
