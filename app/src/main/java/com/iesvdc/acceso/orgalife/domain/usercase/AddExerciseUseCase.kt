package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import javax.inject.Inject

class AddExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    operator fun invoke(exercise: Exercise) {
        exerciseRepository.addExercise(exercise)
    }
}
