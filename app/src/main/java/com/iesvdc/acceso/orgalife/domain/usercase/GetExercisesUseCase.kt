package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    operator fun invoke(): List<Exercise> {
        return exerciseRepository.getExercises() // Devuelve la lista mutable
    }
}
