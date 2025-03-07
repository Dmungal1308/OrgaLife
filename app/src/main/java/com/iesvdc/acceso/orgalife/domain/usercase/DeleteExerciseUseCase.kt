package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import javax.inject.Inject

class DeleteExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: Int) {
        exerciseRepository.deleteExercise(exerciseId)
    }
}
