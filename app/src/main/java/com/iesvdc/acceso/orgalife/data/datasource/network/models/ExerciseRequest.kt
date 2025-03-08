package com.iesvdc.acceso.orgalife.data.datasource.network.models

data class ExerciseRequest(
    val name: String,
    val description: String?,
    val imageBase64: String?
)

data class ExerciseResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val imageBase64: String?,
    val ownerId: Int
)
