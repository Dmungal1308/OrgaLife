package com.iesvdc.acceso.orgalife.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.adapter.ExerciseAdapter
import com.iesvdc.acceso.orgalife.data.Exercise
import com.iesvdc.acceso.orgalife.data.ExerciseRepository
import com.iesvdc.acceso.orgalife.databinding.ActivityMenuBinding

// MenuActivity
class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private val exercises = ExerciseRepository.getExercises() // Lista de ejercicios
    lateinit var adapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cambiar color de la barra de estado (lo mantienes tal como lo tienes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Ajustes para la barra de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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
