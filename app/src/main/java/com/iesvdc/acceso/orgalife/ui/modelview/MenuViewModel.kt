package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import com.iesvdc.acceso.orgalife.domain.usercase.AddExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.DeleteExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.GetExercisesUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.LogoutUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.UpdateExerciseUseCase

class MenuViewModel(application: Application) : AndroidViewModel(application) {

    // Creamos manualmente los UseCases. En un proyecto grande, usarías inyección de dependencias.
    private val repository = ExerciseRepository // Singleton
    private val getExercisesUseCase = GetExercisesUseCase(repository)
    private val addExerciseUseCase = AddExerciseUseCase(repository)
    private val deleteExerciseUseCase = DeleteExerciseUseCase(repository)
    private val updateExerciseUseCase = UpdateExerciseUseCase(repository)
    private val logoutUseCase = LogoutUseCase(getApplication())

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> get() = _exercises

    private val _logoutEvent = MutableLiveData<Boolean>()
    val logoutEvent: LiveData<Boolean> get() = _logoutEvent

    init {
        // Al iniciar, cargamos la lista de ejercicios usando el Use Case
        _exercises.value = getExercisesUseCase().toList()
    }

    fun addExercise(exercise: Exercise) {
        addExerciseUseCase(exercise)
        // Volvemos a cargar la lista
        _exercises.value = getExercisesUseCase().toList()
    }

    fun deleteExercise(exercise: Exercise) {
        deleteExerciseUseCase(exercise)
        _exercises.value = getExercisesUseCase().toList()
    }

    fun updateExercise(oldExercise: Exercise, newExercise: Exercise) {
        updateExerciseUseCase(oldExercise, newExercise)
        _exercises.value = getExercisesUseCase().toList()
    }

    fun logout() {
        logoutUseCase()
        _logoutEvent.value = true
    }

    fun resetLogoutEvent() {
        _logoutEvent.value = false
    }
}
