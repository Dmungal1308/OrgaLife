package com.iesvdc.acceso.orgalife.domain.usercase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// Resultado del login
sealed class LoginResult {
    object Success : LoginResult()
    object EmailNotVerified : LoginResult()
    data class Error(val message: String) : LoginResult()
}

/**
 * Caso de uso para iniciar sesión en Firebase.
 */
class LoginUserUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (email.isBlank() || password.isBlank()) {
            return LoginResult.Error("Por favor, completa todos los campos.")
        }
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = firebaseAuth.currentUser
            if (user != null && user.isEmailVerified) {
                LoginResult.Success
            } else {
                LoginResult.EmailNotVerified
            }
        } catch (e: Exception) {
            LoginResult.Error("Correo o contraseña incorrectos.")
        }
    }
}
