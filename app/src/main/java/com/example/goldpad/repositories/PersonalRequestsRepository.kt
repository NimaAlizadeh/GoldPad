package com.example.goldpad.repositories

import com.example.goldpad.database.dao.RequestDao
import com.example.goldpad.database.entities.Request
import javax.inject.Inject

class PersonalRequestsRepository @Inject constructor(
    private val requestDao: RequestDao
) {
    suspend fun getRequestsByUserId(userId: Int): List<Request> {
        return requestDao.getRequestsByUserId(userId)
    }

    suspend fun insertRequest(request: Request) {
        requestDao.insertRequest(request)
    }

    suspend fun deleteRequest(request: Request) {
        requestDao.deleteRequest(request)
    }

    suspend fun updateRequest(request: Request) {
        requestDao.updateRequest(request)
    }
}
