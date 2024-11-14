package com.example.goldpad.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.goldpad.database.entities.Request

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: Request)

    @Delete
    suspend fun deleteRequest(request: Request)

    @Update
    suspend fun updateRequest(request: Request)

    @Query("SELECT * FROM request WHERE fromUser = :userId")
    suspend fun getRequestsByUserId(userId: Int): List<Request>
}
