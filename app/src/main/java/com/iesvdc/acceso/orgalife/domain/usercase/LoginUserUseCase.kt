package com.iesvdc.acceso.orgalife.domain.usercase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

// Para representar el resultado del login
sealed class LoginResult {
    object Success : LoginResult()
    object EmailNotVerified : LoginResult()
    data class Error(val message: String) : LoginResult()
}

/**
 * Caso de uso para iniciar sesión en Firebase.
 */
class LoginUserUseCase(private val firebaseAuth: FirebaseAuth) {

    /**
     * Usa corutinas para login. Retorna un 'LoginResult' con success, emailNotVerified o error.
     */
    suspend operator fun invoke(email: String, password: String): LoginResult {
        // Validación básica. Podrías hacerlo en la capa del ViewModel también, a gusto.
        if (email.isBlank() || password.isBlank()) {
            return LoginResult.Error("Por favor, completa todos los campos.")
        }

        return try {
            // Usamos coroutines con "await" para no usar listeners
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = firebaseAuth.currentUser

            if (user != null && user.isEmailVerified) {
                // Todo correcto
                LoginResult.Success
            } else {
                // Usuario sin verificar
                LoginResult.EmailNotVerified
            }

        } catch (e: Exception) {
            // Error genérico o mensaje más específico
            LoginResult.Error("Correo o contraseña incorrectos.")
        }
    }
}