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
import com.iesvdc.acceso.orgalife.databinding.ActivityEjercicioBinding
import com.iesvdc.acceso.orgalife.ui.modelview.EjercicioViewModel

class EjercicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEjercicioBinding
    private lateinit var drawerLayout: DrawerLayout

    // Obtenemos el EjercicioViewModel con el delegate 'by viewModels()'
    private val ejercicioViewModel: EjercicioViewModel by viewModels()

    // Instancia del Adapter (sin pasar la lista al constructor)
    private lateinit var adapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEjercicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Ajustes para la barra de sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Creamos el adapter con lambdas que llaman al ViewModel
        adapter = ExerciseAdapter(
            onDeleteClicked = { exercise ->
                // En vez de manipular la lista local, avisamos al ViewModel
                ejercicioViewModel.deleteExercise(exercise)
            },
            onEditClicked = { exercise ->
                showEditExerciseDialog(exercise)
            },
            onAddClicked = {
                showAddExerciseDialog()
            }
        )

        // Configuramos el RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observamos el LiveData del ViewModel
        // y cada vez que cambie la lista, refrescamos el Adapter
        ejercicioViewModel.exercises.observe(this, Observer { updatedExercises ->
            adapter.setExercises(updatedExercises)
        })

        // FAB para agregar un ejercicio
        binding.addExerciseButton.setOnClickListener {
            showAddExerciseDialog()
        }

        // Botón hamburguesa
        binding.imageButton.setOnClickListener {
            toggleDrawer()
        }

        // "Inicio" en el menú lateral → vuelve a MenuActivity
        val inicio = binding.root.findViewById<TextView>(R.id.textView3)
        inicio.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        // Flecha del drawer
        val botonFlecha = binding.root.findViewById<ImageButton>(R.id.botonFlecha)
        botonFlecha.setOnClickListener {
            toggleDrawer()
        }

        // Cerrar sesión del drawer
        val cerrarSesion = binding.root.findViewById<TextView>(R.id.cerrarSesion)
        cerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Barra inferior
        val btnInicio = findViewById<ImageButton>(R.id.btnInicio)
        val btnAnuncios = findViewById<ImageButton>(R.id.btnAnuncios)
        val btnCerrarSesion = findViewById<ImageButton>(R.id.btnCerrarSesion)

        btnInicio.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        btnAnuncios.setOnClickListener {
            startActivity(Intent(this, AnunciosActivity::class.java))
        }
        btnCerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }
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
        EditExerciseDialogFragment.newInstance(exercise)
            .show(supportFragmentManager, "EditExerciseDialog")
    }

    private fun showLogoutConfirmationDialog() {
        // Si quieres, podrías tener un logoutEvent en EjercicioViewModel
        // y un LogoutConfirmationDialogFragment que llame a ejercicioViewModel.logout().
        // O puedes mantenerlo simple y hacerlo igual que en MenuActivity.
        LogoutConfirmationDialogFragment().show(supportFragmentManager, "LogoutConfirmationDialog")
    }
}
