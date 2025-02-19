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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EjercicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEjercicioBinding
    private lateinit var drawerLayout: DrawerLayout

    // Inyectamos el ViewModel con Hilt
    private val ejercicioViewModel: EjercicioViewModel by viewModels()

    // Adapter que se configurará con las acciones delegadas al ViewModel
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

        // Configurar los insets para la barra de sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar RecyclerView y Adapter
        adapter = ExerciseAdapter(
            onDeleteClicked = { exercise ->
                // Delegamos al ViewModel
                ejercicioViewModel.deleteExercise(exercise)
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

        // Observamos el LiveData del ViewModel para actualizar el Adapter
        ejercicioViewModel.exercises.observe(this, Observer { updatedExercises ->
            adapter.setExercises(updatedExercises)
        })

        // Configuramos listeners para botones y otros elementos de la UI
        binding.addExerciseButton.setOnClickListener { showAddExerciseDialog() }
        binding.imageButton.setOnClickListener { toggleDrawer() }
        binding.root.findViewById<ImageButton>(R.id.botonFlecha).setOnClickListener { toggleDrawer() }
        binding.root.findViewById<TextView>(R.id.cerrarSesion).setOnClickListener { showLogoutConfirmationDialog() }
        binding.root.findViewById<TextView>(R.id.textView3).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnAnuncios).setOnClickListener {
            startActivity(Intent(this, AnunciosActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnCerrarSesion).setOnClickListener { showLogoutConfirmationDialog() }
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun showAddExerciseDialog() {
        AddExerciseDialogFragment().show(supportFragmentManager, "AddExerciseDialog")
    }

    private fun showEditExerciseDialog(exercise: Exercise) {
        EditExerciseDialogFragment.newInstance(exercise)
            .show(supportFragmentManager, "EditExerciseDialog")
    }

    private fun showLogoutConfirmationDialog() {
        val dialog = LogoutConfirmationDialogFragment()
        dialog.onLogoutConfirmed = {
            // Aquí navegas al LoginActivity y cierras la Activity actual
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        dialog.show(supportFragmentManager, "LogoutConfirmationDialog")
    }
}
