package com.iesvdc.acceso.orgalife.domain.usercase

import android.content.Context

/**
 * Caso de uso para verificar si hay sesión ya iniciada.
 */
class IsLoggedInUseCase(private val context: Context) {

    operator fun invoke(): Boolean {
        val prefs = context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("isLoggedIn", false)
    }
}