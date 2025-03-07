package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iesvdc.acceso.orgalife.domain.usercase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val loginUserUseCase: LoginUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase, // si lo eliminas, quita esto
    private val saveSessionUseCase: SaveSessionUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
) : AndroidViewModel(application) {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _loginErrorMessage = MutableLiveData<String?>()
    val loginErrorMessage: LiveData<String?> get() = _loginErrorMessage

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            when (val result = loginUserUseCase(email, password)) {
                is LoginResult.Success -> {
                    _loginSuccess.value = true
                    _loginErrorMessage.value = null
                }
                is LoginResult.Error -> {
                    _loginSuccess.value = false
                    _loginErrorMessage.value = result.message
                }

                LoginResult.EmailNotVerified -> TODO()
            }
        }
    }

    fun saveSession() {
        saveSessionUseCase()
    }

    fun isLoggedIn(): Boolean {
        return isLoggedInUseCase()
    }
}
