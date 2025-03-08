package com.iesvdc.acceso.orgalife.domain.usercase


import com.iesvdc.acceso.orgalife.data.repository.AuthRepository
import javax.inject.Inject


import android.util.Log

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        usuario: String,
        nombreCompleto: String,
        correo: String,
        password: String,
        repeatPassword: String
    ): RegistrationResult {
        Log.d("RegisterUserUseCase", "Iniciando registro para: $correo")
        if (correo.isBlank() || password.isBlank()) {
            return RegistrationResult.Error("Por favor, completa todos los campos.")
        }
        if (password != repeatPassword) {
            return RegistrationResult.Error("Las contraseñas no coinciden.")
        }
        return try {
            val result = authRepository.register(correo, password)
            Log.d("RegisterUserUseCase", "Resultado del registro: $result")
            result
        } catch (e: Exception) {
            Log.e("RegisterUserUseCase", "Error en el registro", e)
            RegistrationResult.Error("Error en el registro: ${e.message}")
        }
    }
}

