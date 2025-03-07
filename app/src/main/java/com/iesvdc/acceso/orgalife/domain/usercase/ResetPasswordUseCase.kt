package com.iesvdc.acceso.orgalife.domain.usercase


import javax.inject.Inject

sealed class ResetPasswordResult {
    object Success : ResetPasswordResult()
    data class Error(val message: String) : ResetPasswordResult()
}

/**
 * Caso de uso para restablecer la contraseña vía Firebase.
 */
class ResetPasswordUseCase @Inject constructor(
) {

}
