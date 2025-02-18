package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.iesvdc.acceso.orgalife.domain.usercase.*
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Instanciamos manualmente los Use Cases (en un proyecto grande, inyección de dependencias)
    private val loginUserUseCase = LoginUserUseCase(firebaseAuth)
    private val resetPasswordUseCase = ResetPasswordUseCase(firebaseAuth)
    private val saveSessionUseCase = SaveSessionUseCase(getApplication())
    private val isLoggedInUseCase = IsLoggedInUseCase(getApplication())

    // LiveData para la actividad
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _loginErrorMessage = MutableLiveData<String?>()
    val loginErrorMessage: LiveData<String?> = _loginErrorMessage

    // Para controlar si el usuario debe verificar el email
    private val _emailNotVerified = MutableLiveData<Boolean>()
    val emailNotVerified: LiveData<Boolean> = _emailNotVerified

    // Llamamos al Use Case
    fun loginUser(email: String, password: String) {
        // Usamos corutinas para invocar loginUserUseCase
        viewModelScope.launch {
            val result = loginUserUseCase(email, password)
            when(result) {
                is LoginResult.Success -> {
                    _loginSuccess.value = true
                    _loginErrorMessage.value = null
                }
                is LoginResult.EmailNotVerified -> {
                    _loginSuccess.value = false
                    _emailNotVerified.value = true
                }
                is LoginResult.Error -> {
                    _loginSuccess.value = false
                    _loginErrorMessage.value = result.message
                }
            }
        }
    }

    // Para restablecer contraseña
    fun resetPassword(email: String) {
        viewModelScope.launch {
            val result = resetPasswordUseCase(email)
            when(result) {
                is ResetPasswordResult.Success -> {
                    _loginErrorMessage.value = "Se ha enviado un correo para restablecer tu contraseña."
                }
                is ResetPasswordResult.Error -> {
                    _loginErrorMessage.value = result.message
                }
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
