package com.example.goldpad.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldpad.database.dto.RequestWithUser
import com.example.goldpad.database.entities.Request
import com.example.goldpad.database.entities.Waiting
import com.example.goldpad.repositories.PersonalRequestsRepository
import com.example.goldpad.repositories.WaitingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AllBuyerRequestsViewModel @Inject constructor(
    private val requestRepository: PersonalRequestsRepository,
    private val waitingRepository: WaitingRepository
) : ViewModel() {

    val requestsLiveData = MutableLiveData<List<RequestWithUser>>() // Buyer requests
    val selectedRequests = MutableLiveData<MutableList<RequestWithUser>>(mutableListOf()) // Selected items

    fun fetchBuyerRequests() {
        viewModelScope.launch {
            val buyerRequests = withContext(Dispatchers.IO) {
                requestRepository.getAllRequestsWithUsers()
                    .filter { it.request.mode && it.request.isActive }
            }
            requestsLiveData.postValue(buyerRequests)
        }
    }

    fun toggleRequestSelection(requestWithUser: RequestWithUser) {
        selectedRequests.value?.let { selectedList ->
            if (selectedList.contains(requestWithUser)) {
                selectedList.remove(requestWithUser)
            } else {
                selectedList.add(requestWithUser)
            }
            selectedRequests.postValue(selectedList)
        }
    }

    fun saveSelectedRequestsToWaiting(waitingId: Int, selectedRequests: List<RequestWithUser>) {
        viewModelScope.launch(Dispatchers.IO) {
            val waiting = waitingRepository.getWaitingById(waitingId)
            if (waiting != null) {
                val updatedRequests = waiting.requests.toMutableList()
                updatedRequests.addAll(selectedRequests.map { it.request })
                waitingRepository.updateWaitingRequests(waitingId, updatedRequests)
            }
        }
    }

    fun deleteWaitingRecord(waitingId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            waitingRepository.deleteWaitingById(waitingId)
        }
    }
}