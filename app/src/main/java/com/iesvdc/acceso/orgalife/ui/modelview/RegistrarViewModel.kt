package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iesvdc.acceso.orgalife.domain.models.UserData
import com.iesvdc.acceso.orgalife.domain.usercase.RegisterUserUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.RegistrationResult
import kotlinx.coroutines.launch

class RegistrarViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Instanciamos el Use Case. En un proyecto real, esto se inyectaría.
    private val registerUserUseCase = RegisterUserUseCase(firebaseAuth, firestore)

    // LiveData para manejar éxito y error en el registro
    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    private val _registrationError = MutableLiveData<String?>()
    val registrationError: LiveData<String?> get() = _registrationError

    // LiveData para controlar el estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun registerUser(
        usuario: String,
        nombreCompleto: String,
        correo: String,
        password: String,
        repeatPassword: String
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = registerUserUseCase(usuario, nombreCompleto, correo, password, repeatPassword)
            _isLoading.value = false
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
