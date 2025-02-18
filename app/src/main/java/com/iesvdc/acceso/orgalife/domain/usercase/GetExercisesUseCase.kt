package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.models.Exercise

class GetExercisesUseCase(private val exerciseRepository: ExerciseRepository) {
    operator fun invoke(): List<Exercise> {
        return exerciseRepository.getExercises() // Devuelve la lista mutable
    }
}