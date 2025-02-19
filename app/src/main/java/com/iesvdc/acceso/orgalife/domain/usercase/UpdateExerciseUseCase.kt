package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import javax.inject.Inject

class UpdateExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    operator fun invoke(oldExercise: Exercise, newExercise: Exercise) {
        exerciseRepository.updateExercise(oldExercise, newExercise)
    }
}
