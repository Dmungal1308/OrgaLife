package com.iesvdc.acceso.orgalife.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import com.iesvdc.acceso.orgalife.databinding.DialogAddExerciseBinding
import com.iesvdc.acceso.orgalife.ui.modelview.MenuViewModel
import java.io.ByteArrayOutputStream

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

                // Convertir la imagen a Base64 si existe
                val imageDrawable = binding.imageViewExercise.drawable
                var base64Image: String? = null
                if (imageDrawable != null) {
                    val bitmap = drawableToBitmap(imageDrawable)
                    if (bitmap != null) {
                        base64Image = encodeToBase64(bitmap)
                    }
                }

                if (name.isNotEmpty() && description.isNotEmpty()) {
                    // Para un ejercicio nuevo, usamos id = 0 y ownerId = 0 (el backend asignará estos valores)
                    val newExercise = ExerciseResponse(
                        id = 0,
                        name = name,
                        description = description,
                        imageBase64 = base64Image,
                        ownerId = 0
                    )
                    menuViewModel.addExercise(newExercise)
                    dismiss()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.color.superficieContenedorAlta)

            val titleId = resources.getIdentifier("alertTitle", "id", "android")
            val textViewTitle = dialog.findViewById<TextView>(titleId)
            textViewTitle?.setTextColor(resources.getColor(R.color.primario))

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(resources.getColor(R.color.primario))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                ?.setTextColor(resources.getColor(R.color.primario))
        }

        return dialog
    }

    // Función para convertir un Drawable a Bitmap
    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
            val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    // Función para convertir un Bitmap a Base64
    private fun encodeToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        // Usamos PNG para mantener la calidad; ajusta si es necesario
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
