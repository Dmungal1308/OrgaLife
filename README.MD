# Orgalife

Orgalife es una aplicación diseñada para ayudarte a organizar todos los aspectos de tu vida de manera sencilla y eficiente. Con esta herramienta, podrás gestionar tus rutinas diarias, ya sean de ejercicio, tareas del hogar o incluso tus horarios de clase. La aplicación incluye funcionalidades como:

- Gestión de rutinas personalizadas.
- Seguimiento de actividades mediante listas.
- Gestión de la lista de la compra.
- Diseño amigable y flexible para mejorar tu productividad diaria.

### Características principales:
1. **Inicio de sesión seguro** con validación de usuario.
2. **Vista principal (menú)** para organizar ejercicios con un diseño adaptable.
3. **Eliminación y gestión de ejercicios** mediante un RecyclerView dinámico.
4. Uso de adaptadores para manipular listas de datos eficientemente.

A continuación, se describen las principales clases y componentes utilizados en el proyecto, con ejemplos de código relevantes.

---

## **Índice**
- [Orgalife](#orgalife)
    - [Características principales:](#características-principales)
  - [**Índice**](#índice)
  - [**Estructura del proyecto**](#estructura-del-proyecto)
  - [**Descripción de las clases**](#descripción-de-las-clases)
    - [**1. LoginActivity**](#1-loginactivity)
      - [Código relevante:](#código-relevante)
    - [**2. MenuActivity**](#2-menuactivity)
      - [Código relevante:](#código-relevante-1)
    - [**3. ExerciseAdapter**](#3-exerciseadapter)
    - [**4. ExerciseRepository**](#4-exerciserepository)
  - [**Configuración y ejecución**](#configuración-y-ejecución)
  - [**Futuras mejoras**](#futuras-mejoras)

---

## **Estructura del proyecto**

```
com.iesvdc.acceso.orgalife
├── view
│   ├── LoginActivity.kt
│   ├── MenuActivity.kt
├── adapter
│   ├── ExerciseAdapter.kt
├── data
│   ├── Exercise.kt
│   ├── ExerciseRepository.kt
├── res
│   ├── layout
│   │   ├── activity_login.xml
│   │   ├── activity_menu.xml
│   │   ├── item_exercise.xml
│   ├── mipmap
│   │   ├── ic_icono_principal_foreground.png
│   │   ├── ic_ojo_cerrado.png
│   │   ├── ic_ojo_contrasenna.png
│   │   ├── ic_borrar_foreground.png
│   ├── values
│   │   ├── colors.xml
│   │   ├── strings.xml
```

---

## **Descripción de las clases**

### **1. LoginActivity**

Esta clase se encarga de gestionar el inicio de sesión del usuario. Incluye lógica para alternar la visibilidad de la contraseña y validar el acceso del usuario.

#### Código relevante:

**LoginActivity.kt**
```kotlin
class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.button)
        val togglePasswordButton = findViewById<ImageButton>(R.id.buttonTogglePassword)

        // Alternar visibilidad de la contraseña
        togglePasswordButton.setOnClickListener {
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordButton.setImageResource(R.mipmap.ic_ojo_cerrado)
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
                togglePasswordButton.setImageResource(R.mipmap.ic_ojo_contrasenna)
            }
            isPasswordVisible = !isPasswordVisible
        }

        // Validación del usuario
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username == "root" && password == "1234") {
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
```

**activity_login.xml** (Diseño simplificado de la pantalla de inicio de sesión)
```xml
<EditText
    android:id="@+id/editTextText"
    android:hint="Usuario"
    android:inputType="text" />

<EditText
    android:id="@+id/editTextPassword"
    android:hint="Contraseña"
    android:inputType="textPassword" />

<Button
    android:id="@+id/button"
    android:text="Iniciar sesión" />
```

---

### **2. MenuActivity**

La pantalla principal muestra una lista de ejercicios, que se gestionan dinámicamente mediante un RecyclerView. 

#### Código relevante:

**MenuActivity.kt**
```kotlin
class MenuActivity : AppCompatActivity() {
    private lateinit var adapter: ExerciseAdapter
    private val exercises = ExerciseRepository.getExercises()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        adapter = ExerciseAdapter(exercises) { exercise ->
            exercises.remove(exercise)
            adapter.notifyDataSetChanged()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
```

**activity_menu.xml**
```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

---

### **3. ExerciseAdapter**

El adaptador se utiliza para enlazar los datos con el RecyclerView. Este incluye funcionalidades como mostrar el nombre, descripción e imagen de los ejercicios y un botón para eliminarlos.

**ExerciseAdapter.kt**
```kotlin
class ExerciseAdapter(
    private val exercises: MutableList<Exercise>,
    private val onDeleteClicked: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.nameTextView.text = exercise.name
        holder.descriptionTextView.text = exercise.description
        holder.imageView.setImageResource(exercise.imageResId)

        holder.deleteButton.setOnClickListener { onDeleteClicked(exercise) }
    }

    override fun getItemCount(): Int = exercises.size

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textExerciseName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textExerciseDescription)
        val imageView: ImageView = itemView.findViewById(R.id.imageExercise)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }
}
```

**item_exercise.xml** (Tarjeta de diseño para los elementos del RecyclerView)
```xml
<ImageView
    android:id="@+id/imageExercise"
    android:contentDescription="Imagen del ejercicio" />

<TextView
    android:id="@+id/textExerciseName"
    android:text="Nombre del ejercicio" />

<TextView
    android:id="@+id/textExerciseDescription"
    android:text="Descripción del ejercicio" />

<ImageButton
    android:id="@+id/deleteButton"
    android:src="@mipmap/ic_borrar_foreground" />
```

---

### **4. ExerciseRepository**

Se utiliza para simular una fuente de datos que genera ejercicios.

**ExerciseRepository.kt**
```kotlin
object ExerciseRepository {
    fun getExercises(): MutableList<Exercise> {
        return mutableListOf(
            Exercise("Flexión", "Ejercicio para la parte superior del cuerpo", R.mipmap.ic_icono_principal_foreground),
            Exercise("Sentadilla", "Ejercicio para las piernas", R.mipmap.ic_icono_principal_foreground)
        )
    }
}
```

---

## **Configuración y ejecución**

1. Clona este repositorio.
2. Abre el proyecto en **Android Studio**.
3. Compila y ejecuta en un dispositivo o emulador.

---

## **Futuras mejoras**

1. Implementar autenticación real con bases de datos.
2. Añadir funcionalidades de edición para los ejercicios.
3. Integrar notificaciones para recordatorios de rutinas.

--- 