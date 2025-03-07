package com.iesvdc.acceso.orgalife.data.repository

import android.util.Log
import com.iesvdc.acceso.orgalife.data.datasource.network.ExerciseApi
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseRequest
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    private val exerciseApi: ExerciseApi
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
            // Llama a la API y mapea la respuesta
            exerciseApi.getExercises().map { response ->
                ExerciseResponse(
                    id = response.id,
                    name = response.name,
                    description = response.description,
                    imageBase64 = response.imageBase64,
                    ownerId = response.ownerId
                )
            }
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
        val request = ExerciseRequest(
            name = exercise.name,
            description = exercise.description,
            imageBase64 = exercise.imageBase64
        )
        val response = exerciseApi.updateExercise(exerciseId, request)
        return mapResponseToExercise(response)
    }

    suspend fun deleteExercise(exerciseId: Int) {
        exerciseApi.deleteExercise(exerciseId)
    }
}
