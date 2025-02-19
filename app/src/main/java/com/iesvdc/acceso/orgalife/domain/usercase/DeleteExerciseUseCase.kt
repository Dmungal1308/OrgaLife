package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import javax.inject.Inject

class DeleteExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    operator fun invoke(exercise: Exercise) {
        val exercises = exerciseRepository.getExercises()
        exercises.remove(exercise)
    }
}
