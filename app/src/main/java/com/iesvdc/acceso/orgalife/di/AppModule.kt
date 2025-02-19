package com.iesvdc.acceso.orgalife.di

import android.content.Context
import com.iesvdc.acceso.orgalife.data.repository.ExerciseRepository
import com.iesvdc.acceso.orgalife.domain.usercase.AddExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.DeleteExerciseUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.GetAnunciosUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.GetExercisesUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.LogoutUseCase
import com.iesvdc.acceso.orgalife.domain.usercase.UpdateExerciseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Si tu ExerciseRepository es un objeto (singleton), podrías proveerlo de esta forma:
    @Provides
    @Singleton
    fun provideExerciseRepository(): ExerciseRepository = ExerciseRepository()

    @Provides
    fun provideGetExercisesUseCase(repository: ExerciseRepository): GetExercisesUseCase =
        GetExercisesUseCase(repository)

    @Provides
    fun provideAddExerciseUseCase(repository: ExerciseRepository): AddExerciseUseCase =
        AddExerciseUseCase(repository)

    @Provides
    fun provideDeleteExerciseUseCase(repository: ExerciseRepository): DeleteExerciseUseCase =
        DeleteExerciseUseCase(repository)

    @Provides
    fun provideUpdateExerciseUseCase(repository: ExerciseRepository): UpdateExerciseUseCase =
        UpdateExerciseUseCase(repository)

    @Provides
    fun provideLogoutUseCase(@ApplicationContext context: Context): LogoutUseCase =
        LogoutUseCase(context)

    @Provides
    @Singleton
    fun provideGetAnunciosUseCase(): GetAnunciosUseCase = GetAnunciosUseCase()
}
