package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import com.iesvdc.acceso.orgalife.domain.usercase.AddExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.DeleteExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.GetExercisesUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.UpdateExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EjercicioViewModel @Inject constructor(
    application: Application,
    private val getExercisesUseCase: GetExercisesUseCase,
    private val addExerciseUseCase: AddExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val updateExerciseUseCase: UpdateExerciseUseCase
) : AndroidViewModel(application) {

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> get() = _exercises

    init {
        _exercises.value = getExercisesUseCase()
    }

    fun addExercise(exercise: Exercise) {
        addExerciseUseCase(exercise)
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
