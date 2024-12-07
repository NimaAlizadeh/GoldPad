package com.example.goldpad.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userId: Int, // مرتبط با کاربر
    var mainText: String, // متن اصلی
    var phase: Int = 1 // مقدار پیش‌فرض 1
)
