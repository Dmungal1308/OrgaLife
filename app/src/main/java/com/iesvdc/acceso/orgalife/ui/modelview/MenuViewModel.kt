package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import com.iesvdc.acceso.orgalife.domain.usercase.AddExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.DeleteExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.GetExercisesUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.LogoutUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.UpdateExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    application: Application,
    private val getExercisesUseCase: GetExercisesUseCase,
    private val addExerciseUseCase: AddExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val updateExerciseUseCase: UpdateExerciseUseCase,
    private val logoutUseCase: LogoutUseCase
) : AndroidViewModel(application) {

    private val _exercises = MutableLiveData<List<ExerciseResponse>>()
    val exercises: LiveData<List<ExerciseResponse>> get() = _exercises

    private val _logoutEvent = MutableLiveData<Boolean>()
    val logoutEvent: LiveData<Boolean> get() = _logoutEvent

    init {
        loadExercises()
    }

    fun loadExercises() {
        viewModelScope.launch {
            _exercises.value = getExercisesUseCase().toList()
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
            deleteExerciseUseCase(exercise.id)
            loadExercises()
        }
    }

    fun updateExercise(oldExercise: ExerciseResponse, newExercise: ExerciseResponse) {
        viewModelScope.launch {
            try {
                updateExerciseUseCase(oldExercise.id, newExercise)
                loadExercises()
            } catch (e: Exception) {
                Log.e("MenuViewModel", "Error al actualizar ejercicio", e)
            }
        }
    }


    fun logout() {
        logoutUseCase()
        _logoutEvent.value = true
    }

    fun resetLogoutEvent() {
        _logoutEvent.value = false
    }
}
