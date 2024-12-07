package com.example.goldpad.repositories

import com.example.goldpad.database.dao.RequestDao
import com.example.goldpad.database.entities.Request
import javax.inject.Inject

class RequestRepository @Inject constructor(private val requestDao: RequestDao) {

    suspend fun getActiveRequests(isBuyer: Boolean): List<Request> {
        return requestDao.getActiveRequests(isBuyer)
    }

    suspend fun deactivateRequests(requestIds: List<Int>) {
        requestDao.deactivateRequests(requestIds)
    }
}
