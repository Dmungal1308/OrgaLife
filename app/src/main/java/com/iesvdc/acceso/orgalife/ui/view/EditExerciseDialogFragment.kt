package com.iesvdc.acceso.orgalife.ui.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.data.datasource.network.models.ExerciseResponse
import com.iesvdc.acceso.orgalife.databinding.DialogEditExerciseBinding
import com.iesvdc.acceso.orgalife.ui.modelview.MenuViewModel
import java.io.ByteArrayOutputStream

class EditExerciseDialogFragment : DialogFragment() {

    private var _binding: DialogEditExerciseBinding? = null
    private val binding get() = _binding!!

    // ViewModel para actualizar el ejercicio
    private lateinit var menuViewModel: MenuViewModel

    // Ejercicio que se edita
    private lateinit var exercise: ExerciseResponse

    // Uri donde se guardará la foto tomada (en la galería)
    private var photoUri: Uri? = null

    companion object {
        fun newInstance(exercise: ExerciseResponse): EditExerciseDialogFragment {
            val fragment = EditExerciseDialogFragment()
            fragment.exercise = exercise
            return fragment
        }
    }

    // Solicitar permiso de cámara
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }

    // Resultado de la cámara
    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Glide.with(requireContext())
                    .load(photoUri)
                    .into(binding.imageViewExercise)
            }
        }

    // Resultado de la galería
    private val selectFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                if (selectedImageUri != null) {
                    Glide.with(requireContext())
                        .load(selectedImageUri)
                        .into(binding.imageViewExercise)
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuViewModel = ViewModelProvider(requireActivity())[MenuViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditExerciseBinding.inflate(layoutInflater)

        // Inicializar campos con la info del ejercicio
        binding.editTextName.setText(exercise.name)
        binding.editTextDescription.setText(exercise.description)

        // Si ya hay imagen en Base64, cargarla
        if (!exercise.imageBase64.isNullOrEmpty()) {
            val bytes = Base64.decode(exercise.imageBase64, Base64.DEFAULT)
            Glide.with(requireContext())
                .asBitmap()
                .load(bytes)
                .into(binding.imageViewExercise)
        }

        // Botón "Tomar foto"
        binding.buttonTakePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                openCamera()
            }
        }

        // Botón "Seleccionar de galería"
        binding.buttonSelectFromGallery.setOnClickListener {
            openGallery()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Editar Ejercicio")
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
                    // Se conserva el id y ownerId originales
                    val updatedExercise = ExerciseResponse(
                        id = exercise.id,
                        name = name,
                        description = description,
                        imageBase64 = base64Image,
                        ownerId = exercise.ownerId
                    )
                    menuViewModel.updateExercise(exercise, updatedExercise)
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

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton?.setTextColor(resources.getColor(R.color.primario))
            negativeButton?.setTextColor(resources.getColor(R.color.primario))
        }

        return dialog
    }

    // Abre la cámara y guarda la foto
    private fun openCamera() {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Nueva imagen")
            put(MediaStore.Images.Media.DESCRIPTION, "Foto tomada desde la cámara")
        }
        photoUri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        takePhotoLauncher.launch(intent)
    }

    // Abre la galería
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        selectFromGalleryLauncher.launch(intent)
    }

    // Convierte un Drawable a Bitmap
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

    // Convierte un Bitmap a Base64
    private fun encodeToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
