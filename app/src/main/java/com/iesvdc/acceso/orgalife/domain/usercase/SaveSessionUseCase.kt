package com.iesvdc.acceso.orgalife.domain.usercase

import android.content.Context
import androidx.core.content.edit

/**
 * Caso de uso para guardar la sesión en SharedPreferences.
 */
class SaveSessionUseCase(private val context: Context) {

    operator fun invoke() {
        val prefs = context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        prefs.edit {
            putBoolean("isLoggedIn", true)
        }
    }
}