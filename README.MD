# 📱 OrgaLife

**Autor:** David Muñoz Gallego  
**Tecnologías:** Android Studio, Kotlin, Retrofit, Dagger-Hilt, MVVM, LiveData

## 📚 Descripción del Proyecto

OrgaLife es una aplicación móvil desarrollada en Android Studio con Kotlin. Está diseñada para facilitar a los usuarios la creación, gestión y personalización de sus propias rutinas o ejercicios sobre cualquier temática o actividad. OrgaLife promueve un enfoque personalizado, permitiendo que cada usuario registre ejercicios con descripciones e imágenes asociadas, administrando sesiones de usuario, autenticación segura y gestión completa de contenidos personales.

## 📂 Estructura del Proyecto

La estructura del proyecto OrgaLife sigue una arquitectura limpia basada en MVVM y está organizada en múltiples capas y módulos claramente definidos:

```
com
└── iesvdc
    └── acceso
        └── orgalife
            ├── data
            │   ├── datasource
            │   │   └── network
            │   │       ├── models
            │   │       │   ├── ExerciseRequest
            │   │       │   ├── ExerciseResponse
            │   │       │   ├── LoginRequest
            │   │       │   ├── LoginResponse
            │   │       │   └── RegisterRequest
            │   │       ├── AuthApi
            │   │       ├── ExerciseApi
            │   │       └── AuthInterceptor
            │   └── repository
            │       ├── AuthRepository
            │       └── ExerciseRepository
            ├── di
            │   ├── AppModule
            │   └── NetworkModule
            ├── domain
            │   ├── models
            │   │   ├── Anuncio
            │   │   └── UserData
            │   └── usercase
            │       ├── AddExerciseUseCase
            │       ├── DeleteExerciseUseCase
            │       ├── GetAnunciosUseCase
            │       ├── GetExercisesUseCase
            │       ├── IsLoggedInUseCase
            │       ├── LoginUserUseCase
            │       ├── LogoutUseCase
            │       ├── RegisterUserUseCase
            │       ├── RegistrationResult
            │       ├── ResetPasswordUseCase
            │       ├── SaveSessionUseCase
            │       ├── UpdateExerciseUseCase
            │       └── WaitForSplashUseCase
            └── ui
                ├── adapter
                │   ├── AnuncioAdapter
                │   └── ExerciseAdapter
                ├── modelview
                │   ├── AnunciosViewModel
                │   ├── EjercicioViewModel
                │   ├── LoginViewModel
                │   ├── MenuViewModel
                │   ├── RegistrarViewModel
                │   └── SplashViewModel
                └── view
                    ├── AddExerciseDialogFragment
                    ├── EditExerciseDialogFragment
                    ├── LogoutConfirmationDialogFragment
                    ├── AnunciosActivity
                    ├── EjercicioActivity
                    ├── LoginActivity
                    ├── MenuActivity
                    ├── PantallaCargaActivity
                    └── RegistrarActivity
MyApplication
```

## 📦 Explicación Detallada de las Clases

### 🔹 Data Layer

#### 📌 AuthRepository
Gestiona la lógica relacionada con la autenticación.
```kotlin
suspend fun login(email: String, password: String): LoginResult {
    val response = authApi.login(LoginRequest(email, password))
    response.token?.let { token ->
        prefs.edit().putString("jwt_token", token).apply()
        return LoginResult.Success(UserData(uid = 15, email = email, usuario = email, nombreCompleto = ""))
    } ?: return LoginResult.Error(response.error ?: "Error desconocido")
}
```
Este método verifica el token y lo guarda localmente usando `SharedPreferences`, proporcionando un manejo robusto de errores.

#### 📌 ExerciseRepository
Realiza operaciones CRUD en ejercicios mediante llamadas API.
```kotlin
suspend fun getExercises(): List<ExerciseResponse> {
    return exerciseApi.getExercises().filter { it.ownerId == currentUserId }
}
```
Filtra automáticamente ejercicios según el usuario actual.

### 🔸 Domain Layer

#### 📌 Casos de Uso (UserCases)
Encapsulan tareas específicas como:
- `AddExerciseUseCase`: Añade ejercicios nuevos.
- `DeleteExerciseUseCase`: Elimina ejercicios existentes.
- `LoginUserUseCase`: Gestiona la autenticación.

Ejemplo:
```kotlin
class DeleteExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: Int) = repository.deleteExercise(exerciseId)
}
```
Facilita reutilización y separación de responsabilidades.

### 🔹 DI Layer
Gestiona la creación e inyección de dependencias mediante Dagger-Hilt:
- `AppModule`: Proporciona repositorios y casos de uso.
- `NetworkModule`: Provee Retrofit, APIs e interceptores.

### 🔸 UI Layer
Gestiona todas las interacciones visuales:

#### 📌 ViewModels
Exponen datos observables para la interfaz:
```kotlin
val exercises = MutableLiveData<List<ExerciseResponse>>()
```
Garantizan actualizaciones dinámicas y seguras.

#### 📌 Actividades y Fragments
Interfaces para usuario final con navegación y diálogos personalizados, por ejemplo:

- `MenuActivity`: Muestra ejercicios con soporte para añadir y editar mediante diálogos.
- `LoginActivity`: Autentica usuarios y maneja sesiones.
- `RegistrarActivity`: Facilita registro de nuevos usuarios.

Ejemplo (MenuActivity):
```kotlin
menuViewModel.exercises.observe(this) { list ->
    exerciseAdapter.setExercises(list)
}
```
Actualiza automáticamente la vista cuando cambian los datos.

## 🚀 Características Principales

- Autenticación segura con JWT.
- Gestión dinámica de ejercicios.
- Arquitectura limpia y escalable.
- Uso avanzado de ViewModels y LiveData.

## 🛠️ Tecnologías Usadas

- Android Studio, Kotlin, Retrofit, Dagger-Hilt, Glide

## 🔧 Configuración

1. Clonar repositorio.
2. Importar en Android Studio.
3. Ejecutar en emulador o dispositivo físico.

## 📌 Dependencias Principales

- Retrofit, Gson, Dagger-Hilt, Glide

## 📱 Interfaces Destacadas

- LoginActivity, RegistrarActivity
- PantallaCargaActivity (Splash)
- MenuActivity, EjercicioActivity
- AnunciosActivity

## 📖 Ejemplo de Uso

- Iniciar sesión.
- Administrar ejercicios personalizados.
- Visualizar y añadir imágenes fácilmente.

## ✒️ Autor

- David Muñoz Gallego

¡Disfruta OrgaLife, personaliza tus rutinas y optimiza tu día a día!