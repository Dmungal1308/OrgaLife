package com.iesvdc.acceso.orgalife.domain.usercase

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class ResetPasswordResult {
    object Success : ResetPasswordResult()
    data class Error(val message: String) : ResetPasswordResult()
}

/**
 * Caso de uso para restablecer la contraseña vía Firebase.
 */
class ResetPasswordUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(email: String): ResetPasswordResult {
        if (email.isEmpty()) {
            return ResetPasswordResult.Error("Por favor, ingresa tu correo electrónico.")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ResetPasswordResult.Error("Por favor, ingresa un correo válido.")
        }
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            ResetPasswordResult.Success
        } catch (e: Exception) {
            val errorMessage = when(e) {
                is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                else -> "Error al enviar el correo: ${e.message}"
            }
            ResetPasswordResult.Error(errorMessage)
        }
    }
}
