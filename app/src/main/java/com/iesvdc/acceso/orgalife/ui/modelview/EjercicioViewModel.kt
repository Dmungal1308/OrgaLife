package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import com.iesvdc.acceso.orgalife.domain.usercase.AddExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.DeleteExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.GetExercisesUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.UpdateExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EjercicioViewModel @Inject constructor(
    application: Application,
    private val getExercisesUseCase: GetExercisesUseCase,
    private val addExerciseUseCase: AddExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val updateExerciseUseCase: UpdateExerciseUseCase
) : AndroidViewModel(application) {

    private val _exercises = MutableLiveData<List<ExerciseResponse>>()
    val exercises: LiveData<List<ExerciseResponse>> get() = _exercises

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            // Llama al UseCase suspendido y actualiza la LiveData
            _exercises.value = getExercisesUseCase()
        }
    }

    fun addExercise(exercise: ExerciseResponse) {
        viewModelScope.launch {
            addExerciseUseCase(exercise)
            loadExercises()
        }
    }

    fun deleteExercise(exercise: ExerciseResponse) {
        viewModelScope.launch {
            // Suponiendo que el DeleteExerciseUseCase ahora recibe el id del ejercicio
            deleteExerciseUseCase(exercise.id)
            loadExercises()
        }
    }

    fun updateExercise(oldExercise: ExerciseResponse, newExercise: ExerciseResponse) {
        viewModelScope.launch {
            // Suponiendo que el UpdateExerciseUseCase ahora recibe el id y el nuevo ejercicio
            updateExerciseUseCase(oldExercise.id, newExercise)
            loadExercises()
        }
    }
}
