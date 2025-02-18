package com.iesvdc.acceso.orgalife.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.ui.adapter.ExerciseAdapter
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import com.iesvdc.acceso.orgalife.databinding.ActivityMenuBinding
import com.iesvdc.acceso.orgalife.ui.modelview.MenuViewModel

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var drawerLayout: DrawerLayout

    // Instanciamos el ViewModel con el delegado "by viewModels()"
    private val menuViewModel: MenuViewModel by viewModels()

    // Creamos el adapter sin lista; pasamos lambdas para acciones CRUD
    private val exerciseAdapter = ExerciseAdapter(
        onDeleteClicked = { exercise -> menuViewModel.deleteExercise(exercise) },
        onEditClicked = { exercise -> showEditExerciseDialog(exercise) },
        onAddClicked = { showAddExerciseDialog() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)

        // Ajuste de StatusBar en versiones modernas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Ajuste para la barra de sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuramos el RecyclerView con LayoutManager + Adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = exerciseAdapter

        // Botón FAB para añadir ejercicios
        binding.addExerciseButton.setOnClickListener {
            showAddExerciseDialog()
        }

        // Botón hamburguesa para el Drawer
        binding.imageButton.setOnClickListener {
            toggleDrawer()
        }

        // Flecha dentro del Drawer
        val botonFlecha = binding.root.findViewById<ImageButton>(R.id.botonFlecha)
        botonFlecha.setOnClickListener {
            toggleDrawer()
        }

        // "Cerrar Sesión" en el Drawer
        val cerrarSesion = binding.root.findViewById<TextView>(R.id.cerrarSesion)
        cerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Texto "ejercicio" que abre EjercicioActivity (o lo que sea)
        val ejercicio = binding.root.findViewById<TextView>(R.id.ejercicio2)
        ejercicio.setOnClickListener {
            startActivity(Intent(this, EjercicioActivity::class.java))
        }

        // Botones de la barra inferior
        val btnAnuncios = findViewById<ImageButton>(R.id.btnAnuncios)
        btnAnuncios.setOnClickListener {
            startActivity(Intent(this, AnunciosActivity::class.java))
        }
        val btnCerrarSesion = findViewById<ImageButton>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // OBSERVAMOS la lista de ejercicios del ViewModel
        menuViewModel.exercises.observe(this, Observer { list ->
            // Cada vez que cambie la lista, actualizamos el adapter
            exerciseAdapter.setExercises(list)
        })
        menuViewModel.logoutEvent.observe(this) { isLoggedOut ->
            if (isLoggedOut == true) {
                menuViewModel.resetLogoutEvent()
                // Navegar al Login
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun showLogoutConfirmationDialog() {
        // Simplemente mostramos el diálogo, sin callback
        LogoutConfirmationDialogFragment().show(supportFragmentManager, "LogoutConfirmationDialog")
    }


    private fun showAddExerciseDialog() {
        // Abrimos el diálogo para agregar
        AddExerciseDialogFragment().show(supportFragmentManager, "AddExerciseDialog")
    }

    private fun showEditExerciseDialog(exercise: Exercise) {
        // Abrimos el diálogo para editar
        EditExerciseDialogFragment.newInstance(exercise)
            .show(supportFragmentManager, "EditExerciseDialog")
    }
}
