package com.iesvdc.acceso.orgalife.domain.usercase

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.iesvdc.acceso.orgalife.domain.models.UserData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class RegisterUserUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend operator fun invoke(
        usuario: String,
        nombreCompleto: String,
        correo: String,
        password: String,
        repeatPassword: String
    ): RegistrationResult {
        // Validaciones...
        // ... (tu código)
        return try {
            firebaseAuth.createUserWithEmailAndPassword(correo, password).await()
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            val user = firebaseAuth.currentUser ?: return RegistrationResult.Error("Error: usuario nulo")
            val userData = UserData(
                uid = user.uid,
                usuario = usuario,
                nombreCompleto = nombreCompleto,
                email = correo
            )
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
                else -> "Error en el registro: ${e.message}"
            }
            RegistrationResult.Error(errorMsg)
        }
    }
}