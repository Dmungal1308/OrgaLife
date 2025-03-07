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
    // Declaración de variables para los elementos de la interfaz
    private lateinit var editTextUsuario: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegistrar: Button
    private lateinit var buttonTogglePassword: ImageButton
    private lateinit var textViewForgotPassword: TextView
    private lateinit var auth: FirebaseAuth
    private var passwordVisible = false // Controla la visibilidad de la contraseña

    // Método que se llama al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configuración de la barra de estado para dispositivos con Android M o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        // Verificar si la sesión ya está iniciada
        val sharedPreferences = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // Si ya está logueado, redirigir a la actividad del menú
            startActivity(Intent(this, MenuActivity::class.java))
            finish() // Finaliza la actividad actual
            return
        }

        setContentView(R.layout.activity_login) // Establece el diseño de la actividad

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        initViews() // Inicializa las vistas
        setupListeners() // Configura los listeners para los botones
    }

    // Método para inicializar las vistas
    private fun initViews() {
        editTextUsuario = findViewById(R.id.editTextUsuario)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegistrar = findViewById(R.id.buttonRegistrar)
        buttonTogglePassword = findViewById(R.id.buttonTogglePassword)
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword)
    }

    // Método para configurar los listeners de los botones
    private fun setupListeners() {
        // Listener para el botón de mostrar/ocultar contraseña
        buttonTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible
            togglePasswordVisibility() // Cambia la visibilidad de la contraseña
        }

        // Listener para el botón de inicio de sesión
        buttonLogin.setOnClickListener {
            val email = editTextUsuario.text.toString().trim()
            val password = editTextPassword.text.toString()

            // Verifica si las credenciales son las predeterminadas
            if (email == "admin" && password == "1234") {
                saveSession() // Guarda la sesión
                startActivity(Intent(this, MenuActivity::class.java)) // Redirige al menú
                finish() // Finaliza la actividad actual
            } else {
                loginUser(email, password) // Intenta iniciar sesión con Firebase
            }
        }

        // Listener para el botón de registro
        buttonRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java)) // Redirige a la actividad de registro
        }

        // Listener para el enlace de "Olvidé mi contraseña"
        textViewForgotPassword.setOnClickListener {
            val email = editTextUsuario.text.toString().trim()
            if (email.isEmpty()) {
                showToast("Por favor, ingresa tu correo electrónico") // Mensaje de error si el campo está vacío
                return@setOnClickListener
            }

            // Verifica si el correo tiene un formato válido
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Por favor, ingresa un correo válido") // Mensaje de error si el formato es incorrecto
                return@setOnClickListener
            }

            // Envía un correo para restablecer la contraseña
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Se ha enviado un correo para restablecer tu contraseña") // Mensaje de éxito
                } else {
                    // Manejo de errores al enviar el correo
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                        else -> "Error al enviar el correo: ${task.exception?.message}"
                    }
                    showToast(errorMessage) // Muestra el mensaje de error
                }
            }
        }
    }

    // Método para alternar la visibilidad de la contraseña
    private fun togglePasswordVisibility() {
        if (passwordVisible) {
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            buttonTogglePassword.setImageResource(R.mipmap.ic_ojo_contrasenna_foreground) // Cambia al ícono de ojo abierto
        } else {
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            buttonTogglePassword.setImageResource(R.mipmap.ic_ojo_cerrado_foreground) // Cambia al ícono de ojo cerrado
        }
        editTextPassword.setSelection(editTextPassword.text.length) // Mantiene el cursor al final del texto
    }

    // Método para iniciar sesión con Firebase
    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        saveSession() // Guarda la sesión
                        startActivity(Intent(this, MenuActivity::class.java)) // Redirige al menú
                        finish() // Finaliza la actividad actual
                    } else {
                        showToast("Por favor, verifica tu correo electrónico") // Mensaje si el correo no está verificado
                        user?.sendEmailVerification() // Envía un correo de verificación
                    }
                } else {
                    showToast("Correo o contraseña incorrectos") // Mensaje de error si las credenciales son incorrectas
                }
            }
        } else {
            showToast("Por favor, completa todos los campos") // Mensaje si los campos están vacíos
        }
    }

    // Método para guardar la sesión del usuario
    private fun saveSession() {
        val sharedPreferences = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true) // Marca la sesión como iniciada
            apply() // Aplica los cambios
        }
    }

    // Método para mostrar mensajes emergentes (Toast)
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show() // Muestra el mensaje
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

    private lateinit var binding: ActivityMenuBinding // Binding para la vista
    private val exercises = ExerciseRepository.getExercises() // Lista de ejercicios
    lateinit var adapter: ExerciseAdapter // Adaptador para el RecyclerView
    private lateinit var drawerLayout: DrawerLayout // DrawerLayout para el menú lateral

    // Metodo que se llama al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater) // Inflar el layout
        setContentView(binding.root) // Establecer la vista

        // Inicializar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Cambiar color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Ajustes para la barra de sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el adaptador con todas las funciones
        adapter = ExerciseAdapter(
            exercises,
            onDeleteClicked = { exercise ->
                exercises.remove(exercise) // Eliminar ejercicio de la lista
                adapter.notifyDataSetChanged() // Notificar cambios al adaptador
            },
            onEditClicked = { exercise ->
                showEditExerciseDialog(exercise) // Mostrar diálogo de edición
            },
            onAddClicked = {
                showAddExerciseDialog() // Mostrar diálogo para agregar ejercicio
            }
        )

        // Configuración del RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this) // Establecer el layout manager
        binding.recyclerView.adapter = adapter // Establecer el adaptador

        // Configurar el click listener para el FloatingActionButton
        binding.addExerciseButton.setOnClickListener {
            showAddExerciseDialog() // Mostrar diálogo para agregar ejercicio
        }

        // Configurar el click listener para el ImageButton
        binding.imageButton.setOnClickListener {
            toggleDrawer() // Alternar el menú lateral
        }

        // Configurar el click listener para la flecha en el menú
        val botonFlecha = binding.root.findViewById<ImageButton>(R.id.botonFlecha)
        botonFlecha.setOnClickListener {
            toggleDrawer() // Cerrar el menú
        }

        // Configurar el click listener para "Cerrar Sesión"
        val cerrarSesion = binding.root.findViewById<TextView>(R.id.cerrarSesion)
        cerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog() // Mostrar diálogo de confirmación de cierre de sesión
        }

        // Configurar el click listener para el ejercicio específico
        val ejercicio = binding.root.findViewById<TextView>(R.id.ejercicio2)
        ejercicio.setOnClickListener {
            val intent = Intent(this, EjercicioActivity::class.java) // Crear intención para iniciar EjercicioActivity
            startActivity(intent) // Iniciar la nueva actividad
        }
    }

    // Metodo para mostrar el diálogo para agregar un ejercicio
    private fun showAddExerciseDialog() {
        AddExerciseDialogFragment().show(supportFragmentManager, "AddExerciseDialog")
    }

    // Metodo para mostrar el diálogo para editar un ejercicio
    private fun showEditExerciseDialog(exercise: Exercise) {
        EditExerciseDialogFragment.newInstance(exercise).show(
            supportFragmentManager,
            "EditExerciseDialog"
        )
    }

    // Metodo para alternar el menú lateral
    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START) // Cerrar el menú
        } else {
            drawerLayout.openDrawer(GravityCompat.START) // Abrir el menú
        }
    }

    // Metodo para mostrar el diálogo de confirmación de cierre de sesión
    private fun showLogoutConfirmationDialog() {
        val dialog = LogoutConfirmationDialogFragment { confirmed ->
            if (confirmed) {
                // Limpiar SharedPreferences para terminar la sesión
                val sharedPreferences = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    remove("isLoggedIn") // Eliminar la marca de sesión iniciada
                    apply() // Aplicar cambios
                }

                // Redirigir al login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent) // Iniciar la actividad de inicio de sesión
                finish() // Cerrar la actividad actual
            }
        }
        dialog.show(supportFragmentManager, "LogoutConfirmationDialog") // Mostrar el diálogo
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
 