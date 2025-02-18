package com.iesvdc.acceso.orgalife.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import com.iesvdc.acceso.orgalife.databinding.DialogAddExerciseBinding
import com.iesvdc.acceso.orgalife.ui.modelview.MenuViewModel

class AddExerciseDialogFragment : DialogFragment() {

    private var _binding: DialogAddExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var menuViewModel: MenuViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Obtenemos el mismo ViewModel que la Activity
        menuViewModel = ViewModelProvider(requireActivity()).get(MenuViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddExerciseBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Añadir Ejercicio")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val name = binding.editTextName.text.toString().trim()
                val description = binding.editTextDescription.text.toString().trim()

                if (name.isNotEmpty() && description.isNotEmpty()) {
                    val newExercise = Exercise(
                        name = name,
                        description = description,
                        imageResId = R.mipmap.ic_icono_principal_foreground
                    )
                    menuViewModel.addExercise(newExercise)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
