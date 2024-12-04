package com.example.goldpad.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.goldpad.database.dto.WaitingWithRequestsDTO
import com.example.goldpad.database.entities.Waiting

@Dao
interface WaitingDao {

    @Query("SELECT * FROM waiting WHERE userId = :userId")
    suspend fun getWaitingItemsByUserId(userId: Int): List<Waiting>

    @Query("SELECT * FROM waiting WHERE id = :waitingId")
    suspend fun getWaitingById(waitingId: Int): Waiting

    @Transaction
    @Query("SELECT * FROM waiting WHERE id = :waitingId")
    suspend fun getWaitingWithRequests(waitingId: Int): WaitingWithRequestsDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaiting(waiting: Waiting): Long

    @Query("DELETE FROM waiting WHERE id = :waitingId")
    suspend fun deleteWaitingById(waitingId: Int)
}
