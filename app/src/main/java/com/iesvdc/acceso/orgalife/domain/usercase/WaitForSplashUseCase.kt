package com.iesvdc.acceso.orgalife.domain.usercase

import kotlinx.coroutines.delay

/**
 * Caso de uso que espera un tiempo determinado y retorna true.
 */
class WaitForSplashUseCase {
    suspend operator fun invoke(delayTimeMillis: Long = 1000): Boolean {
        delay(delayTimeMillis)
        return true
    }
}