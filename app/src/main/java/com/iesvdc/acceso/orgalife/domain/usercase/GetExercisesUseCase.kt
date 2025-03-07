package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(): List<ExerciseResponse> = exerciseRepository.getExercises()
}
