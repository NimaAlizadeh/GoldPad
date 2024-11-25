package com.example.goldpad.repositories

import com.example.goldpad.database.dao.WaitingDao
import com.example.goldpad.database.entities.Waiting
import javax.inject.Inject

class WaitingRepository @Inject constructor(
    private val waitingDao: WaitingDao
) {
    suspend fun insertWaitingItem(waiting: Waiting) {
        waitingDao.insertWaitingItem(waiting)
    }

    suspend fun getWaitingItemsByUserId(userId: Int): List<Waiting> {
        return waitingDao.getWaitingItemsByUserId(userId)
    }
}
