package com.example.goldpad.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldpad.database.entities.Request
import com.example.goldpad.repositories.PersonalRequestsRepository
import com.example.goldpad.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PersonalRequestsViewModel @Inject constructor(
    private val requestRepository: PersonalRequestsRepository
) : ViewModel() {

    val requestsLiveData = MutableLiveData<List<Request>>()

    fun fetchRequests() {

        viewModelScope.launch {
            val userId = Constants.USER_ID
            val requests = withContext(Dispatchers.IO) {
//                requestRepository.insertRequest(Request(fromUser = 4, amount = 10, mode = false))
//                requestRepository.insertRequest(Request(fromUser = 4, amount = 5, mode = false))
                requestRepository.getRequestsByUserId(userId)
            }
            requestsLiveData.postValue(requests)
        }
    }

    fun insertRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            requestRepository.insertRequest(request)
            // Refresh the list after insertion
            fetchRequests()
        }
    }

    fun deleteRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            requestRepository.deleteRequest(request)
            fetchRequests()
        }
    }

    fun updateRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            requestRepository.updateRequest(request)
            fetchRequests()
        }
    }

}
