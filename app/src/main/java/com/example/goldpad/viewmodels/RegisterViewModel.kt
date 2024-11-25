package com.example.goldpad.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldpad.repositories.RegisterRepository
import com.example.goldpad.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> = _registerState

    fun register(username: String, password: String, firstName: String, lastName: String, bankName: String, userId: String, shebaCode: String) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                val user = withContext(Dispatchers.IO) {
                    registerRepository.registerUser(username, password, firstName, lastName, bankName, userId, shebaCode)
                }

                Constants.USER_ID = user.id

                _registerState.postValue(RegisterState.Success(isAdmin = user.isAdmin))

            } catch (e: Exception) {
                _registerState.postValue(RegisterState.Error("عملیات ثبت نام با مشکل مواجه شد! دوباره تلاش کنید"))
            }
        }
    }


}

sealed class RegisterState {
    object Loading : RegisterState()
    data class Success(val isAdmin: Boolean) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
