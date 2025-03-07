package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import javax.inject.Inject

class UpdateExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    // Devuelve el objeto actualizado o lanza un error si es null
    suspend operator fun invoke(exerciseId: Int, exercise: ExerciseResponse): ExerciseResponse {
        return exerciseRepository.updateExercise(exerciseId, exercise)
    }

}
