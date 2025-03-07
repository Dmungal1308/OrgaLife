package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.repository.AuthRepository
import javax.inject.Inject
import android.util.Log

sealed class LoginResult {
    object Success : LoginResult()
    object EmailNotVerified : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class LoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (email.isBlank() || password.isBlank()) {
            Log.d("LoginUserUseCase", "Campos vacíos")
            return LoginResult.Error("Por favor, completa todos los campos.")
        }
        Log.d("LoginUserUseCase", "Invoking login for email: $email")
        return authRepository.login(email, password)
    }
}
