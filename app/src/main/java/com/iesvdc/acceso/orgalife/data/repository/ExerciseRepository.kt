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
                Exercise("Flexión", "Ejercicio para la parte superior del cuerpo", R.mipmap.ic_icono_principal_foreground),
                Exercise("Sentadilla", "Ejercicio para la parte inferior del cuerpo", R.mipmap.ic_icono_principal_foreground),
                Exercise("Plancha", "Ejercicio de fuerza en el core", R.mipmap.ic_icono_principal_foreground),
                Exercise("Press de banca", "Ejercicio de fuerza para el pecho (pesas)", R.mipmap.ic_icono_principal_foreground),
                Exercise("Curl de bíceps", "Ejercicio para los bíceps (pesas)", R.mipmap.ic_icono_principal_foreground),
                Exercise("Peso muerto", "Ejercicio para espalda y piernas (pesas)", R.mipmap.ic_icono_principal_foreground),
                Exercise("Remo con barra", "Ejercicio para la espalda (pesas)", R.mipmap.ic_icono_principal_foreground),
                Exercise("Sentadilla con barra", "Sentadilla con pesas para las piernas", R.mipmap.ic_icono_principal_foreground),
                Exercise("Press militar", "Ejercicio de hombros (pesas)", R.mipmap.ic_icono_principal_foreground),
                Exercise("Zancadas", "Ejercicio para las piernas", R.mipmap.ic_icono_principal_foreground),
                Exercise("Fondos", "Ejercicio para los tríceps", R.mipmap.ic_icono_principal_foreground),
                Exercise("Ab twist", "Ejercicio para el core", R.mipmap.ic_icono_principal_foreground),
                Exercise("Pull-up", "Ejercicio para espalda y bíceps", R.mipmap.ic_icono_principal_foreground),
                Exercise("Dips", "Ejercicio para tríceps", R.mipmap.ic_icono_principal_foreground),
                Exercise("Crunches", "Ejercicio para abdominales", R.mipmap.ic_icono_principal_foreground),
                Exercise("Russian twist", "Ejercicio para oblicuos", R.mipmap.ic_icono_principal_foreground),
                Exercise("Elevación de piernas", "Ejercicio para abdominales", R.mipmap.ic_icono_principal_foreground),
                Exercise("Curls de pierna", "Ejercicio para piernas (pesas)", R.mipmap.ic_icono_principal_foreground),
                Exercise("Press de pierna", "Ejercicio para muslos (pesas)", R.mipmap.ic_icono_principal_foreground)
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
