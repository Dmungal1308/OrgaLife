package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.models.Exercise

class DeleteExerciseUseCase(private val exerciseRepository: ExerciseRepository) {
    operator fun invoke(exercise: Exercise) {
        val exercises = exerciseRepository.getExercises()
        exercises.remove(exercise)
    }
}