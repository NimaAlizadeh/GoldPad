package com.example.goldpad.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldpad.repositories.AuthRepository
import com.example.goldpad.utils.Constants
import com.example.goldpad.utils.generateUserToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val user = authRepository.login(username, password)
            if (user != null) {
                val token = generateUserToken()

                withContext(Dispatchers.IO) {
                    authRepository.saveToken(token)
                    authRepository.updateTokenForUser(user.id, token)
                    authRepository.saveUserId(user.id)
                }

                Constants.USER_ID = user.id

                _loginState.postValue(LoginState.Success)
            } else {
                _loginState.postValue(LoginState.Error("نام کاربری یا رمز عبور نامعتبر است"))
            }
        }
    }
}

sealed class LoginState {
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
