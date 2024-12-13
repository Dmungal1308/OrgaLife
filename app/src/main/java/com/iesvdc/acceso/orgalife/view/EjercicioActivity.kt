package com.iesvdc.acceso.orgalife.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.adapter.ExerciseAdapter
import com.iesvdc.acceso.orgalife.data.Exercise
import com.iesvdc.acceso.orgalife.data.ExerciseRepository
import com.iesvdc.acceso.orgalife.databinding.ActivityEjercicioBinding
import com.iesvdc.acceso.orgalife.databinding.ActivityMenuBinding

class EjercicioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEjercicioBinding
    private val exercises = ExerciseRepository.getExercises() // Lista de ejercicios
    lateinit var adapter: ExerciseAdapter
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEjercicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Cambiar color de la barra de estado (lo mantienes tal como lo tienes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Ajustes para la barra de sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets -> // Cambia 'findViewById(R.id.main)' por 'binding.main'
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el adaptador con todas las funciones
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

        // Configuración del RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Configurar el click listener para el FloatingActionButton
        binding.addExerciseButton.setOnClickListener {
            showAddExerciseDialog()
        }

        // Configurar el click listener para el ImageButton
        binding.imageButton.setOnClickListener {
            toggleDrawer()
        }

        // Configurar el click listener para la flecha en el menú
        val botonFlecha = binding.root.findViewById<ImageButton>(R.id.botonFlecha)
        botonFlecha.setOnClickListener {
            toggleDrawer() // Cerrar el menú
        }

        // Configurar el click listener para "Cerrar Sesión"
        val cerrarSesion = binding.root.findViewById<TextView>(R.id.cerrarSesion)
        cerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        val menu = binding.root.findViewById<TextView>(R.id.textView3)
        menu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
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

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun showLogoutConfirmationDialog() {
        val dialog = LogoutConfirmationDialogFragment { confirmed ->
            if (confirmed) {
                // Redirigir al login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Opcional: cerrar la actividad actual
            }
        }
        dialog.show(supportFragmentManager, "LogoutConfirmationDialog")
    }
}