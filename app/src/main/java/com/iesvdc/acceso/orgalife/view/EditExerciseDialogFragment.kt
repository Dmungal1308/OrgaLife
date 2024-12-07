import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.data.Exercise
import com.iesvdc.acceso.orgalife.data.ExerciseRepository
import com.iesvdc.acceso.orgalife.databinding.DialogEditExerciseBinding
import com.iesvdc.acceso.orgalife.view.MenuActivity
import android.widget.TextView

class EditExerciseDialogFragment : DialogFragment() {
    private lateinit var binding: DialogEditExerciseBinding
    private lateinit var exercise: Exercise

    companion object {
        fun newInstance(exercise: Exercise): EditExerciseDialogFragment {
            val fragment = EditExerciseDialogFragment()
            fragment.exercise = exercise
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditExerciseBinding.inflate(layoutInflater)
        
        // Pre-poblar los campos
        binding.editTextName.setText(exercise.name)
        binding.editTextDescription.setText(exercise.description)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Editar Ejercicio")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val name = binding.editTextName.text.toString()
                val description = binding.editTextDescription.text.toString()
                
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    val updatedExercise = Exercise(
                        name = name,
                        description = description,
                        imageResId = R.mipmap.ic_icono_principal_foreground
                    )
                    ExerciseRepository.updateExercise(exercise, updatedExercise)
                    (activity as? MenuActivity)?.adapter?.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        // Cambiar colores y fondo después de crear el dialog
        dialog.setOnShowListener {
            // Cambiar color del fondo del diálogo
            dialog.window?.setBackgroundDrawableResource(R.color.superficieContenedorAlta)
            
            // Cambiar color del título
            val titleId = resources.getIdentifier("alertTitle", "id", "android")
            val textViewTitle = dialog.findViewById<TextView>(titleId)
            textViewTitle?.setTextColor(resources.getColor(R.color.primario))
            
            // Cambiar color de los botones
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.primario))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.primario))
        }

        return dialog
    }
} 