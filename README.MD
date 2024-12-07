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
      - [Código:](#código)
    - [**2. MenuActivity**](#2-menuactivity)
      - [Código:](#código-1)
    - [**3. ExerciseAdapter**](#3-exerciseadapter)
      - [Código:](#código-2)
    - [**4. ExerciseRepository**](#4-exerciserepository)
      - [Código:](#código-3)
    - [**5. AddExerciseDialogFragment**](#5-addexercisedialogfragment)
      - [Código:](#código-4)
    - [**6. EditExerciseDialogFragment**](#6-editexercisedialogfragment)
      - [Código:](#código-5)
    - [**7. PantallaCargaActivity**](#7-pantallacargaactivity)
      - [Código:](#código-6)
    - [**8. Exercise (Data Class)**](#8-exercise-data-class)
      - [Código:](#código-7)
    - [**Layouts principales**](#layouts-principales)
      - [**activity\_menu.xml**](#activity_menuxml)
      - [**item\_exercise.xml**](#item_exercisexml)
      - [**dialog\_add\_exercise.xml**](#dialog_add_exercisexml)
      - [**dialog\_edit\_exercise.xml**](#dialog_edit_exercisexml)

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
│   │   ├─ item_exercise.xml
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
Métodos principales:
- `onCreate()`: Inicializa la actividad y configura los listeners para:
  - Validar credenciales al hacer clic en el botón de login
  - Alternar la visibilidad de la contraseña
  - Gestionar la navegación a MenuActivity

#### Código:
```kotlin:app/src/main/java/com/iesvdc/acceso/orgalife/view/LoginActivity.kt
class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.button)
        val togglePasswordButton = findViewById<ImageButton>(R.id.buttonTogglePassword)

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

        togglePasswordButton.setOnClickListener {
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordButton.setImageResource(R.mipmap.ic_ojo_cerrado_foreground)
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
                togglePasswordButton.setImageResource(R.mipmap.ic_ojo_contrasenna_foreground)
            }
            isPasswordVisible = !isPasswordVisible
        }
    }
}
```

### **2. MenuActivity**
Métodos principales:
- `onCreate()`: Configura la interfaz principal y el RecyclerView
- `showAddExerciseDialog()`: Muestra el diálogo para añadir nuevos ejercicios
- `showEditExerciseDialog()`: Abre el diálogo de edición con los datos del ejercicio seleccionado
- `onResume()`: Actualiza la lista de ejercicios cuando se vuelve a la actividad

#### Código:
```kotlin:app/src/main/java/com/iesvdc/acceso/orgalife/view/MenuActivity.kt
class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private val exercises = ExerciseRepository.getExercises()
    lateinit var adapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ExerciseAdapter(
            exercises,
            onDeleteClicked = { exercise ->
                exercises.remove(exercise)
                adapter.notifyDataSetChanged()
            },
            onEditClicked = { exercise ->
                showEditExerciseDialog(exercise)
            },
            onAddClicked = { 
                showAddExerciseDialog()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.addExerciseButton.setOnClickListener {
            showAddExerciseDialog()
        }
    }

    private fun showAddExerciseDialog() {
        AddExerciseDialogFragment().show(supportFragmentManager, "AddExerciseDialog")
    }

    private fun showEditExerciseDialog(exercise: Exercise) {
        EditExerciseDialogFragment.newInstance(exercise).show(
            supportFragmentManager, 
            "EditExerciseDialog"
        )
    }
}
```

### **3. ExerciseAdapter**
Métodos principales:
- `onCreateViewHolder()`: Crea nuevas vistas para los elementos del RecyclerView
- `onBindViewHolder()`: Vincula los datos de un ejercicio con su vista correspondiente
- `bind()`: Configura los elementos visuales y listeners para cada ejercicio
- `getItemCount()`: Retorna el número total de ejercicios en la lista

#### Código:
```kotlin:app/src/main/java/com/iesvdc/acceso/orgalife/adapter/ExerciseAdapter.kt
class ExerciseAdapter(
    private val exercises: List<Exercise>,
    private val onDeleteClicked: (Exercise) -> Unit,
    private val onEditClicked: (Exercise) -> Unit,
    private val onAddClicked: () -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemExerciseBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.textExerciseName.text = exercise.name
            binding.textExerciseDescription.text = exercise.description
            binding.imageExercise.setImageResource(exercise.imageResId)

            binding.deleteButton.setOnClickListener {
                onDeleteClicked(exercise)
            }

            binding.editButton.setOnClickListener {
                onEditClicked(exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount() = exercises.size
}
```

### **4. ExerciseRepository**
Métodos principales:
- `getExercises()`: Retorna la lista completa de ejercicios
- `updateExercise()`: Actualiza un ejercicio existente con nueva información
- `addExercise()`: Añade un nuevo ejercicio a la lista
- `removeExercise()`: Elimina un ejercicio específico de la lista

#### Código:
```kotlin:app/src/main/java/com/iesvdc/acceso/orgalife/data/ExerciseRepository.kt
object ExerciseRepository {
    private val exercises = mutableListOf<Exercise>()
    
    init {
        exercises.addAll(
            listOf(
                Exercise("Flexión", "Ejercicio para la parte superior del cuerpo", 
                    R.mipmap.ic_icono_principal_foreground),
                Exercise("Sentadilla", "Ejercicio para la parte inferior del cuerpo", 
                    R.mipmap.ic_icono_principal_foreground),
                Exercise("Plancha", "Ejercicio de fuerza en el core", 
                    R.mipmap.ic_icono_principal_foreground)
                // ... más ejercicios ...
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
```

### **5. AddExerciseDialogFragment**
Métodos principales:
- `onCreateDialog()`: Crea y configura el diálogo para añadir ejercicios
- `validateFields()`: Verifica que los campos requeridos estén completos
- `createExercise()`: Crea una nueva instancia de Exercise con los datos ingresados
- `clearFields()`: Limpia los campos del formulario

#### Código:
```kotlin:app/src/main/java/com/iesvdc/acceso/orgalife/view/AddExerciseDialogFragment.kt
class AddExerciseDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddExerciseBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddExerciseBinding.inflate(layoutInflater)
        
        return AlertDialog.Builder(requireContext())
            .setTitle("Añadir Ejercicio")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val name = binding.editTextName.text.toString()
                val description = binding.editTextDescription.text.toString()
                
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    val newExercise = Exercise(name, description, 
                        R.mipmap.ic_icono_principal_foreground)
                    ExerciseRepository.addExercise(newExercise)
                    (activity as? MenuActivity)?.adapter?.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}
```

### **6. EditExerciseDialogFragment**
Métodos principales:
- `newInstance()`: Crea una nueva instancia del fragmento con los datos del ejercicio
- `onCreateDialog()`: Configura el diálogo con los datos del ejercicio a editar
- `updateExercise()`: Actualiza el ejercicio con los nuevos datos
- `prepopulateFields()`: Rellena los campos con los datos existentes

#### Código:
```kotlin:app/src/main/java/com/iesvdc/acceso/orgalife/view/EditExerciseDialogFragment.kt
class EditExerciseDialogFragment : DialogFragment() {
    private lateinit var binding: DialogEditExerciseBinding
    private lateinit var exercise: Exercise

    companion object {
        fun newInstance(exercise: Exercise): EditExerciseDialogFragment {
            val fragment = EditExerciseDialogFragment()
            fragment.exercise = exercise
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditExerciseBinding.inflate(layoutInflater)
        
        binding.editTextName.setText(exercise.name)
        binding.editTextDescription.setText(exercise.description)

        return AlertDialog.Builder(requireContext())
            .setTitle("Editar Ejercicio")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val name = binding.editTextName.text.toString()
                val description = binding.editTextDescription.text.toString()
                
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    val updatedExercise = Exercise(name, description, 
                        R.mipmap.ic_icono_principal_foreground)
                    ExerciseRepository.updateExercise(exercise, updatedExercise)
                    (activity as? MenuActivity)?.adapter?.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}
```

### **7. PantallaCargaActivity**
Métodos principales:
- `onCreate()`: Inicializa la pantalla de carga
- `startLoginActivity()`: Inicia la transición a la pantalla de login
- `setupUI()`: Configura los elementos visuales de la pantalla de carga

#### Código:
```kotlin:app/src/main/java/com/iesvdc/acceso/orgalife/view/PantallaCargaActivity.kt
class PantallaCargaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_carga)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@PantallaCargaActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // Retraso de 2 segundos
    }
}
```

### **8. Exercise (Data Class)**
Propiedades principales:
- `name`: Almacena el nombre del ejercicio
- `description`: Contiene la descripción detallada
- `imageResId`: Guarda el ID del recurso de imagen asociado

#### Código:
```kotlin:app/src/main/java/com/iesvdc/acceso/orgalife/data/Exercise.kt
data class Exercise(
    val name: String,        // Nombre del ejercicio
    val description: String, // Descripción detallada
    val imageResId: Int      // ID del recurso de imagen
)
```

### **Layouts principales**

#### **activity_menu.xml**
Layout principal que define la estructura de la pantalla de menú:
- RecyclerView para lista de ejercicios
- FloatingActionButton para añadir ejercicios
- Barra superior personalizada
- Sistema de navegación intuitivo

#### **item_exercise.xml**
Plantilla para cada elemento de ejercicio:
- Diseño en formato tarjeta (CardView)
- Área para imagen del ejercicio
- Sección de información detallada
- Botones de acción (editar/eliminar)

#### **dialog_add_exercise.xml**
Layout para el diálogo de creación:
- Campos de entrada validados
- Botones de acción
- Diseño adaptativo

#### **dialog_edit_exercise.xml**
Layout para el diálogo de edición:
- Campos pre-poblados
- Sistema de validación
- Interfaz consistente
 