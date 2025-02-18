package com.iesvdc.acceso.orgalife.domain.usercase

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.iesvdc.acceso.orgalife.domain.models.UserData
import kotlinx.coroutines.tasks.await

class RegisterUserUseCase(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    // Función suspend que devuelve un RegistrationResult
    suspend operator fun invoke(
        usuario: String,
        nombreCompleto: String,
        correo: String,
        password: String,
        repeatPassword: String
    ): RegistrationResult {
        // Validación de campos
        if (usuario.isEmpty() || nombreCompleto.isEmpty() ||
            correo.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()
        ) {
            return RegistrationResult.Error("Todos los campos son obligatorios")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            return RegistrationResult.Error("Formato de correo inválido")
        }
        if (password != repeatPassword) {
            return RegistrationResult.Error("Las contraseñas no coinciden")
        }
        if (password.length < 6) {
            return RegistrationResult.Error("La contraseña debe tener al menos 6 caracteres")
        }

        return try {
            // Crear usuario en FirebaseAuth
            firebaseAuth.createUserWithEmailAndPassword(correo, password).await()
            // Enviar email de verificación
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            val user = firebaseAuth.currentUser
                ?: return RegistrationResult.Error("Error: usuario nulo")
            // Crear el objeto UserData (asegúrate de tener esta data class en el dominio)
            val userData = UserData(
                uid = user.uid,
                usuario = usuario,
                nombreCompleto = nombreCompleto,
                email = correo
            )
            // Guardar datos en Firestore
            firestore.collection("usuarios")
                .document(user.uid)
                .set(userData)
                .await()

            RegistrationResult.Success
        } catch (e: Exception) {
            val errorMsg = when (e) {
                is FirebaseAuthWeakPasswordException ->
                    "La contraseña debe tener al menos 6 caracteres"
                is FirebaseAuthInvalidCredentialsException ->
                    "El correo electrónico no es válido"
                is FirebaseAuthUserCollisionException ->
                    "Ya existe una cuenta con este correo electrónico"
                else ->
                    "Error en el registro: ${e.message}"
            }
            RegistrationResult.Error(errorMsg)
        }
    }
}