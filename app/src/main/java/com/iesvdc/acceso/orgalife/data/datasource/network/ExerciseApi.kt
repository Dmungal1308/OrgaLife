package com.iesvdc.acceso.orgalife.data.datasource.network

import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseRequest
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExerciseApi {

    @GET("exercises")
    suspend fun getExercises(): List<ExerciseResponse>

    @POST("exercises")
    suspend fun addExercise(@Body exercise: ExerciseRequest): ExerciseResponse

    @PUT("exercises/{id}")
    suspend fun updateExercise(
        @Path("id") id: Int,
        @Body exercise: ExerciseRequest
    ): ExerciseResponse

    @DELETE("exercises/{id}")
    suspend fun deleteExercise(@Path("id") id: Int)
}
