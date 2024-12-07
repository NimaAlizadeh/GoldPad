package com.example.goldpad.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldpad.database.entities.Notification
import com.example.goldpad.database.entities.Request
import com.example.goldpad.database.entities.Waiting
import com.example.goldpad.repositories.NotificationRepository
import com.example.goldpad.repositories.WaitingRepository
import com.example.goldpad.utils.NotificationTexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminConfirmationViewModel @Inject constructor(
    private val waitingRepository: WaitingRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val waitingLiveData = MutableLiveData<Waiting>()

    fun fetchWaitingRecord(waitingId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val waiting = waitingRepository.getWaitingById(waitingId)
            waitingLiveData.postValue(waiting)
        }
    }

    fun confirmRequestsAndCreateNotifications(waitingId: Int, proposedValues: Map<Request, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val waiting = waitingRepository.getWaitingById(waitingId)
            waiting?.let { data ->
                data.requests.forEach { request ->
                    val proposedValue = proposedValues[request]?.toDoubleOrNull() ?: 0.0
                    request.proposedValue = proposedValue
                    request.isActive = false // Deactivate the request

                    // Create notification for user
                    val action = if (request.mode) "خرید" else "فروش"
                    val mainText = NotificationTexts.PHASE_1.template.format(
                        action,
                        request.amount,
                        proposedValue
                    )
                    val notification = Notification(
                        userId = request.userId,
                        mainText = mainText
                    )
                    notificationRepository.insertNotification(notification)
                }
                waitingRepository.updateWaitingRequests(waitingId, data.requests)
            }
        }
    }
}
