package com.iesvdc.acceso.orgalife.data.repository

import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor() {
    private val exercises = mutableListOf<Exercise>()

    init {
        exercises.addAll(
            listOf(
                Exercise("Flexión", "Ejercicio para la parte superior del cuerpo", null),
                Exercise("Sentadilla", "Ejercicio para la parte inferior del cuerpo", null),
                Exercise("Plancha", "Ejercicio de fuerza en el core", null),
                Exercise("Press de banca", "Ejercicio de fuerza para el pecho (pesas)", null),
                Exercise("Curl de bíceps", "Ejercicio para los bíceps (pesas)", null),
                Exercise("Peso muerto", "Ejercicio para espalda y piernas (pesas)", null),
                Exercise("Remo con barra", "Ejercicio para la espalda (pesas)", null),
                Exercise("Sentadilla con barra", "Sentadilla con pesas para las piernas", null),
                Exercise("Press militar", "Ejercicio de hombros (pesas)", null),
                Exercise("Zancadas", "Ejercicio para las piernas",null),
                Exercise("Fondos", "Ejercicio para los tríceps",null)
            )
        )
    }

    fun getExercises(): MutableList<Exercise> = exercises

    fun updateExercise(oldExercise: Exercise, newExercise: Exercise) {
        val index = exercises.indexOf(oldExercise)
        if (index != -1) {
            exercises[index] = newExercise
        }
    }

    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
    }
}
