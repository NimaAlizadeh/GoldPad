package com.example.goldpad.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldpad.database.dto.WaitingWithRequestsDTO
import com.example.goldpad.database.entities.Request
import com.example.goldpad.repositories.WaitingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminConfirmationViewModel @Inject constructor(
    private val waitingRepository: WaitingRepository
) : ViewModel() {

    val waitingWithRequestsLiveData = MutableLiveData<WaitingWithRequestsDTO>()

    fun fetchWaitingWithRequests(waitingId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val waitingWithRequests = waitingRepository.getWaitingWithRequests(waitingId)
            waitingWithRequestsLiveData.postValue(waitingWithRequests)
        }
    }

    fun updateProposedValue(request: Request, value: String) {
        request.proposedValue = value.toDoubleOrNull()
    }

    fun confirmRequests(waitingId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val waitingWithRequests = waitingRepository.getWaitingWithRequests(waitingId)
            waitingWithRequests?.let {
                // Logic for confirming requests
            }
        }
    }
}
