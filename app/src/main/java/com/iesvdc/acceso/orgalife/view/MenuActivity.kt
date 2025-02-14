package com.iesvdc.acceso.orgalife.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.view.GravityCompat
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.adapter.ExerciseAdapter
import com.iesvdc.acceso.orgalife.data.Exercise
import com.iesvdc.acceso.orgalife.data.ExerciseRepository
import com.iesvdc.acceso.orgalife.databinding.ActivityMenuBinding

// Clase principal de la actividad del menú
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
        val btnAnuncios = findViewById<ImageButton>(R.id.btnAnuncios)
        btnAnuncios.setOnClickListener {
            // Navegar a AnunciosActivity
            startActivity(Intent(this, AnunciosActivity::class.java))
        }

        // Botón de Cerrar Sesión
        val btnCerrarSesion = findViewById<ImageButton>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
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