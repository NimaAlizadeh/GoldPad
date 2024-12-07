package com.example.goldpad.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.goldpad.database.entities.Notification

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification): Long

    @Query("SELECT * FROM notification WHERE userId = :userId")
    suspend fun getNotificationsByUserId(userId: Int): List<Notification>
}
