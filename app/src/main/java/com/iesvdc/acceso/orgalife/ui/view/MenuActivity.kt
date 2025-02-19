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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var drawerLayout: DrawerLayout

    // Inyectamos el ViewModel con Hilt
    private val menuViewModel: MenuViewModel by viewModels()

    // Creamos el adapter (sin lista inicial)
    private val exerciseAdapter = ExerciseAdapter(
        onDeleteClicked = { exercise -> menuViewModel.deleteExercise(exercise) },
        onEditClicked = { exercise -> showEditExerciseDialog(exercise) },
        onAddClicked = { showAddExerciseDialog() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = exerciseAdapter

        binding.addExerciseButton.setOnClickListener { showAddExerciseDialog() }
        binding.imageButton.setOnClickListener { toggleDrawer() }
        binding.root.findViewById<ImageButton>(R.id.botonFlecha).setOnClickListener { toggleDrawer() }
        binding.root.findViewById<TextView>(R.id.cerrarSesion).setOnClickListener { showLogoutConfirmationDialog() }
        binding.root.findViewById<TextView>(R.id.ejercicio2).setOnClickListener {
            startActivity(Intent(this, EjercicioActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnAnuncios).setOnClickListener {
            startActivity(Intent(this, AnunciosActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnCerrarSesion).setOnClickListener { showLogoutConfirmationDialog() }
        findViewById<ImageButton>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        // Observamos los LiveData del ViewModel
        menuViewModel.exercises.observe(this, Observer { list ->
            exerciseAdapter.setExercises(list)
        })
        menuViewModel.logoutEvent.observe(this) { isLoggedOut ->
            if (isLoggedOut) {
                menuViewModel.resetLogoutEvent()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun showLogoutConfirmationDialog() {
        LogoutConfirmationDialogFragment().show(supportFragmentManager, "LogoutConfirmationDialog")
    }

    private fun showAddExerciseDialog() {
        AddExerciseDialogFragment().show(supportFragmentManager, "AddExerciseDialog")
    }

    private fun showEditExerciseDialog(exercise: Exercise) {
        EditExerciseDialogFragment.newInstance(exercise).show(supportFragmentManager, "EditExerciseDialog")
    }
}
