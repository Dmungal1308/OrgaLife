package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import javax.inject.Inject

class UpdateExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: Int, exercise: ExerciseResponse): ExerciseResponse =
        exerciseRepository.updateExercise(exerciseId, exercise)
}
