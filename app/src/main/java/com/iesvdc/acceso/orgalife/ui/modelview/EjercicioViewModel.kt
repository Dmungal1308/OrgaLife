package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.usercase.GetExercisesUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.AddExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.DeleteExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.UpdateExerciseUseCase

class EjercicioViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia manual del repositorio (o inyectada)
    private val repository = ExerciseRepository

    // Creamos los Use Cases
    private val getExercisesUseCase = GetExercisesUseCase(repository)
    private val addExerciseUseCase = AddExerciseUseCase(repository)
    private val deleteExerciseUseCase = DeleteExerciseUseCase(repository)
    private val updateExerciseUseCase = UpdateExerciseUseCase(repository)

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> get() = _exercises

    init {
        // Cargamos la lista inicial a través del Use Case
        _exercises.value = getExercisesUseCase()
    }

    fun addExercise(exercise: Exercise) {
        addExerciseUseCase(exercise)
        // Actualizamos la lista usando el Use Case
        _exercises.value = getExercisesUseCase()
    }

    fun deleteExercise(exercise: Exercise) {
        deleteExerciseUseCase(exercise)
        _exercises.value = getExercisesUseCase()
    }

    fun updateExercise(oldExercise: Exercise, newExercise: Exercise) {
        updateExerciseUseCase(oldExercise, newExercise)
        _exercises.value = getExercisesUseCase()
    }
}
