package com.iesvdc.acceso.orgalife.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.data.Exercise
import com.iesvdc.acceso.orgalife.data.ExerciseRepository
import com.iesvdc.acceso.orgalife.databinding.DialogAddExerciseBinding

class AddExerciseDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddExerciseBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddExerciseBinding.inflate(layoutInflater)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Añadir Ejercicio")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val name = binding.editTextName.text.toString()
                val description = binding.editTextDescription.text.toString()
                
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    val newExercise = Exercise(name, description, R.mipmap.ic_icono_principal_foreground)
                    ExerciseRepository.addExercise(newExercise)
                    (activity as? MenuActivity)?.adapter?.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.color.superficieContenedorAlta)
            
            val titleId = resources.getIdentifier("alertTitle", "id", "android")
            val textViewTitle = dialog.findViewById<TextView>(titleId)
            textViewTitle?.setTextColor(resources.getColor(R.color.primario))
            
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.primario))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.primario))
        }

        return dialog
    }
} 