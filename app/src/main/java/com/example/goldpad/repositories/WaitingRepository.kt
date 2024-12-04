package com.example.goldpad.repositories

import com.example.goldpad.database.dao.WaitingDao
import com.example.goldpad.database.dto.WaitingWithRequestsDTO
import com.example.goldpad.database.entities.Request
import com.example.goldpad.database.entities.Waiting
import javax.inject.Inject

class WaitingRepository @Inject constructor(
    private val waitingDao: WaitingDao
) {

    suspend fun getWaitingById(waitingId: Int): Waiting? {
        return waitingDao.getWaitingById(waitingId)
    }

    suspend fun updateWaitingRequests(waitingId: Int, updatedRequests: List<Request>) {
        val waiting = waitingDao.getWaitingById(waitingId)
        if (waiting != null) {
            val updatedWaiting = waiting.copy(requests = updatedRequests)
            waitingDao.insertWaiting(updatedWaiting)
        }
    }

    suspend fun insertWaitingItem(waiting: Waiting): Long {
        return waitingDao.insertWaiting(waiting)
    }

    suspend fun getWaitingWithRequests(waitingId: Int): WaitingWithRequestsDTO? {
        return waitingDao.getWaitingWithRequests(waitingId)
    }

    suspend fun deleteWaitingById(waitingId: Int) {
        waitingDao.deleteWaitingById(waitingId)
    }
}
