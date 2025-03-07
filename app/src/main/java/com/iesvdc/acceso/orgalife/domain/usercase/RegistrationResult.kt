package com.iesvdc.acceso.orgalife.domain.usercase

sealed class RegistrationResult {
    object Success : RegistrationResult()
    data class Error(val message: String) : RegistrationResult()
}