package com.iesvdc.acceso.orgalife.data.repository

import android.content.Context
import android.util.Log
import com.iesvdc.acceso.orgalife.data.datasource.network.ExerciseApi
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseRequest
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    private val exerciseApi: ExerciseApi,
    @ApplicationContext private val context: Context
) {
    // Convierte ExerciseResponse a Exercise
    private fun mapResponseToExercise(response: ExerciseResponse): ExerciseResponse {
        return ExerciseResponse(
            id = response.id,
            name = response.name,
            description = response.description,
            imageBase64 = response.imageBase64,
            ownerId = response.ownerId
        )
    }

    suspend fun getExercises(): List<ExerciseResponse> {
        return try {
            val response = exerciseApi.getExercises()
            Log.d("ExerciseRepository", "Respuesta de getExercises: $response")
            // Si filtras por ownerId, verifica el valor de currentUserId
            // Después de recibir la respuesta exitosa de login:
            val prefs = context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
            val currentUserId = prefs.getInt("user_id", 15)
            Log.d("ExerciseRepository", "currentUserId: $currentUserId")
            response.filter { it.ownerId == currentUserId }
        } catch (e: retrofit2.HttpException) {
            Log.e("ExerciseRepository", "HTTP Exception en getExercises: ${e.code()} - ${e.message()}")
            emptyList()
        } catch (e: Exception) {
            Log.e("ExerciseRepository", "Exception en getExercises", e)
            emptyList()
        }
    }

    suspend fun addExercise(exercise: ExerciseResponse): ExerciseResponse {
        // Crea un ExerciseRequest a partir del Exercise
        val request = ExerciseRequest(
            name = exercise.name,
            description = exercise.description,
            imageBase64 = exercise.imageBase64
        )
        val response = exerciseApi.addExercise(request)
        return mapResponseToExercise(response)
    }

    suspend fun updateExercise(exerciseId: Int, exercise: ExerciseResponse): ExerciseResponse {
        // Creas el request
        val request = ExerciseRequest(
            name = exercise.name,
            description = exercise.description,
            imageBase64 = exercise.imageBase64
        )
        // Llamas al API
        val response = exerciseApi.updateExercise(exerciseId, request)
        // Mapeas la respuesta a tu modelo local
        return mapResponseToExercise(response)
    }


    suspend fun deleteExercise(exerciseId: Int) {
        exerciseApi.deleteExercise(exerciseId)
    }
}
