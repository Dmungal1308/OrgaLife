package com.iesvdc.acceso.orgalife.domain.models

data class Exercise(
    val name: String,
    val description: String,
    val imageBase64: String? = null
)
