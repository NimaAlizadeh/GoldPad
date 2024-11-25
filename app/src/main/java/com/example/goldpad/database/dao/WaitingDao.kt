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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaitingItem(waiting: Waiting)

    @Query("SELECT * FROM waiting WHERE userId = :userId")
    suspend fun getWaitingItemsByUserId(userId: Int): List<Waiting>

    @Transaction
    @Query("SELECT * FROM waiting WHERE id = :waitingId")
    fun getWaitingWithRequests(waitingId: Int): WaitingWithRequestsDTO
}
