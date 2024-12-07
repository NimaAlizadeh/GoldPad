package com.example.goldpad.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldpad.database.entities.Request
import com.example.goldpad.repositories.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    val activeRequestsLiveData = MutableLiveData<List<Request>>()

    fun fetchActiveRequests(isBuyer: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val requests = requestRepository.getActiveRequests(isBuyer)
            activeRequestsLiveData.postValue(requests)
        }
    }
}
