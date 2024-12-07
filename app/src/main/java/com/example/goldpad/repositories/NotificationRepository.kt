package com.example.goldpad.repositories

import com.example.goldpad.database.dao.NotificationDao
import com.example.goldpad.database.entities.Notification
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao
) {
    suspend fun insertNotification(notification: Notification): Long {
        return notificationDao.insertNotification(notification)
    }

    suspend fun getNotificationsByUserId(userId: Int): List<Notification> {
        return notificationDao.getNotificationsByUserId(userId)
    }
}
