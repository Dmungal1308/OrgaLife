package com.iesvdc.acceso.orgalife.data.datasource.network.models

data class ExerciseRequest(
    val name: String,
    val description: String?,
    val imageBase64: String?
    // El ownerId lo extraerás del token en el backend o lo envías desde el cliente, según tu implementación
)

data class ExerciseResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val imageBase64: String?,
    val ownerId: Int
)
