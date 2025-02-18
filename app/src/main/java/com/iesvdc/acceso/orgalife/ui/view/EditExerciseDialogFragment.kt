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
import com.iesvdc.acceso.orgalife.databinding.DialogEditExerciseBinding
import com.iesvdc.acceso.orgalife.ui.modelview.MenuViewModel

class EditExerciseDialogFragment : DialogFragment() {

    private var _binding: DialogEditExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var exercise: Exercise

    companion object {
        fun newInstance(exercise: Exercise): EditExerciseDialogFragment {
            val fragment = EditExerciseDialogFragment()
            fragment.exercise = exercise
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuViewModel = ViewModelProvider(requireActivity()).get(MenuViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditExerciseBinding.inflate(layoutInflater)

        binding.editTextName.setText(exercise.name)
        binding.editTextDescription.setText(exercise.description)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Editar Ejercicio")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val name = binding.editTextName.text.toString().trim()
                val description = binding.editTextDescription.text.toString().trim()

                if (name.isNotEmpty() && description.isNotEmpty()) {
                    val updatedExercise = Exercise(
                        name = name,
                        description = description,
                        imageResId = exercise.imageResId
                    )
                    menuViewModel.updateExercise(exercise, updatedExercise)
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
