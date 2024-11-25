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
class AllSellerRequestsViewModel @Inject constructor(
    private val requestRepository: PersonalRequestsRepository,
    private val waitingRepository: WaitingRepository
) : ViewModel() {

    val requestsLiveData = MutableLiveData<List<Request>>()
    val selectedRequests = MutableLiveData<MutableList<RequestWithUser>>(mutableListOf())
    val requestsWithUsersLiveData = MutableLiveData<List<RequestWithUser>>()


    fun fetchRequests() {
        viewModelScope.launch {
            val requestsWithUsers = withContext(Dispatchers.IO) {
                requestRepository.getAllRequestsWithUsers()
            }
            requestsWithUsersLiveData.postValue(requestsWithUsers)
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

    fun saveSelectedRequestsToWaiting(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedRequests.value?.forEach { requestWithUser ->
                val waiting = Waiting(requestId = requestWithUser.request.id, userId = userId)
                waitingRepository.insertWaitingItem(waiting)
            }
        }
    }

}
