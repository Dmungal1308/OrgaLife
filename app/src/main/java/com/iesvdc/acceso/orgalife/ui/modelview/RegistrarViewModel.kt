package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iesvdc.acceso.orgalife.domain.usercase.RegisterUserUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.RegistrationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrarViewModel @Inject constructor(
    application: Application,
    private val registerUserUseCase: RegisterUserUseCase
) : AndroidViewModel(application) {

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    private val _registrationError = MutableLiveData<String?>()
    val registrationError: LiveData<String?> get() = _registrationError

    fun registerUser(
        usuario: String,
        nombreCompleto: String,
        correo: String,
        password: String,
        repeatPassword: String
    ) {
        viewModelScope.launch {
            val result = registerUserUseCase(usuario, nombreCompleto, correo, password, repeatPassword)
            when (result) {
                is RegistrationResult.Success -> {
                    _registrationSuccess.value = true
                    _registrationError.value = null
                }
                is RegistrationResult.Error -> {
                    _registrationSuccess.value = false
                    _registrationError.value = result.message
                }
            }
        }
    }
}
