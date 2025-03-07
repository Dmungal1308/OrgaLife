package com.iesvdc.acceso.orgalife.di

import android.content.Context
import com.iesvdc.acceso.orgalife.data.datasource.network.AuthApi
import com.iesvdc.acceso.orgalife.data.datasource.network.AuthInterceptor
import com.iesvdc.acceso.orgalife.data.datasource.network.ExerciseApi
import com.iesvdc.acceso.orgalife.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // Inyecta el OkHttpClient aquí
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.147:8080/")
            .client(okHttpClient) // Usa el cliente que añade el token
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExerciseApi(retrofit: Retrofit): ExerciseApi {
        return retrofit.create(ExerciseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepository(authApi, context)
    }
}
