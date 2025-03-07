package com.iesvdc.acceso.orgalife.data.datasource.network

import com.iesvdc.acceso.orgalife.data.datasource.network.models.LoginRequest
import com.iesvdc.acceso.orgalife.data.datasource.network.models.RegisterRequest

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Map<String, String>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Map<String, String>
}
