package com.iesvdc.acceso.orgalife.data.repository

import android.content.Context
import android.util.Log
import com.iesvdc.acceso.orgalife.data.datasource.network.AuthApi
import com.iesvdc.acceso.orgalife.data.datasource.network.models.LoginRequest
import com.iesvdc.acceso.orgalife.data.datasource.network.models.RegisterRequest
import com.iesvdc.acceso.orgalife.domain.models.UserData
import com.iesvdc.acceso.orgalife.domain.usercase.LoginResult
import com.iesvdc.acceso.orgalife.domain.usercase.RegistrationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    @ApplicationContext private val context: Context
) {
    suspend fun login(email: String, password: String): LoginResult {
        Log.d("AuthRepository", "login called with email: $email")
        return try {
            val response = authApi.login(LoginRequest(email, password))
            Log.d("AuthRepository", "Response from API: $response")
            val token = response.token
            if (!token.isNullOrEmpty()) {
                val prefs = context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putString("jwt_token", token)
                    putInt("user_id", 15) // o el id real que te devuelva la API
                    apply()
                }
                Log.d("AuthRepository", "Guardado user_id: ${prefs.getInt("user_id", -1)}")

                Log.d("AuthRepository", "Token guardado en SharedPreferences: ${prefs.getString("jwt_token", "null")}")

                val userData = UserData(
                    uid = 15,  // Extrae el id real del token o de la respuesta
                    usuario = email,
                    nombreCompleto = "Desconocido",
                    email = email
                )

                LoginResult.Success(userData)
            } else {
                Log.e("AuthRepository", "Error de API: ${response.error}")
                LoginResult.Error(response.error ?: "Error desconocido")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception in login", e)
            LoginResult.Error("Error en el login: ${e.message}")
        }
    }

    suspend fun register(email: String, password: String): RegistrationResult {
        return try {
            val response = authApi.register(RegisterRequest(email, password))
            if (response.error != null) {
                RegistrationResult.Error(response.error)
            } else {
                RegistrationResult.Success
            }
        } catch (e: Exception) {
            RegistrationResult.Error("Error en el registro: ${e.message}")
        }
    }
}
