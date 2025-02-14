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
    private lateinit var drawerLayout: DrawerLayout

    private val exercises = ExerciseRepository.getExercises()
    private lateinit var adapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Inflas el binding
        binding = ActivityEjercicioBinding.inflate(layoutInflater)
        // 2) setContentView con la root del binding
        setContentView(binding.root)

        // 3) DrawerLayout
        drawerLayout = binding.drawerLayout

        // Ajustes de la barra de estado
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

        // Configurar RecyclerView
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

        // FAB
        binding.addExerciseButton.setOnClickListener {
            showAddExerciseDialog()
        }

        // Botón hamburguesa (drawer)
        binding.imageButton.setOnClickListener {
            toggleDrawer()
        }

        val inicio = binding.root.findViewById<TextView>(R.id.textView3)
        inicio.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java) // Crear intención para iniciar MenuActivity
            startActivity(intent) // Iniciar la nueva actividad
        }

        // Flecha en el menú lateral
        val botonFlecha = binding.root.findViewById<ImageButton>(R.id.botonFlecha)
        botonFlecha.setOnClickListener {
            toggleDrawer()
        }

        // Cerrar sesión en el menú lateral
        val cerrarSesion = binding.root.findViewById<TextView>(R.id.cerrarSesion)
        cerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Barra inferior:
        val btnInicio = findViewById<ImageButton>(R.id.btnInicio)
        val btnAnuncios = findViewById<ImageButton>(R.id.btnAnuncios)
        val btnCerrarSesion = findViewById<ImageButton>(R.id.btnCerrarSesion)

        btnInicio.setOnClickListener {
            // Ir a MenuActivity
            startActivity(Intent(this, MenuActivity::class.java))
        }
        btnAnuncios.setOnClickListener {
            // Ir a AnunciosActivity
            startActivity(Intent(this, AnunciosActivity::class.java))
        }
        btnCerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Si tienes más elementos (ej. un TextView "menu" para volver):
        // val menu = binding.root.findViewById<TextView>(R.id.textView3)
        // menu.setOnClickListener { startActivity(Intent(this, MenuActivity::class.java)) }
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun showAddExerciseDialog() {
        AddExerciseDialogFragment().show(supportFragmentManager, "AddExerciseDialog")
    }

    private fun showEditExerciseDialog(exercise: Exercise) {
        EditExerciseDialogFragment.newInstance(exercise).show(supportFragmentManager, "EditExerciseDialog")
    }

    private fun showLogoutConfirmationDialog() {
        val dialog = LogoutConfirmationDialogFragment { confirmed ->
            if (confirmed) {
                // Redirigir al login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        dialog.show(supportFragmentManager, "LogoutConfirmationDialog")
    }
}
