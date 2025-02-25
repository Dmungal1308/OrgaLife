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
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import com.iesvdc.acceso.orgalife.databinding.DialogEditExerciseBinding
import com.iesvdc.acceso.orgalife.ui.modelview.MenuViewModel
import java.io.ByteArrayOutputStream
import com.bumptech.glide.Glide


class EditExerciseDialogFragment : DialogFragment() {

    private var _binding: DialogEditExerciseBinding? = null
    private val binding get() = _binding!!

    // ViewModel para actualizar el ejercicio
    private lateinit var menuViewModel: MenuViewModel

    // Ejercicio que se edita (o null si es nuevo)
    private lateinit var exercise: Exercise

    // Uri donde se guardará la foto tomada (en la galería)
    private var photoUri: Uri? = null

    companion object {
        fun newInstance(exercise: Exercise): EditExerciseDialogFragment {
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
                // Mostrar en el ImageView la foto recién tomada usando Glide
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
                    // Mostrar en el ImageView
                    Glide.with(requireContext())
                        .load(selectedImageUri)
                        .into(binding.imageViewExercise)
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Obtener el ViewModel si lo necesitas
        menuViewModel = ViewModelProvider(requireActivity())[MenuViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditExerciseBinding.inflate(layoutInflater)

        // Inicializar campos con la info del ejercicio (si ya existe)
        binding.editTextName.setText(exercise.name)
        binding.editTextDescription.setText(exercise.description)

        // Si el ejercicio ya tenía una imagen en Base64, la cargamos con Glide
        if (!exercise.imageBase64.isNullOrEmpty()) {
            val bytes = Base64.decode(exercise.imageBase64, Base64.DEFAULT)
            Glide.with(requireContext())
                .asBitmap()
                .load(bytes) // Aquí cargamos directamente el array de bytes
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

        // Construimos el diálogo
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Editar Ejercicio")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val name = binding.editTextName.text.toString().trim()
                val description = binding.editTextDescription.text.toString().trim()

                if (name.isNotEmpty() && description.isNotEmpty()) {
                    // A la hora de guardar, convertimos lo que esté en el ImageView a Base64
                    val imageDrawable = binding.imageViewExercise.drawable
                    var base64Image: String? = null
                    if (imageDrawable != null) {
                        val bitmap = drawableToBitmap(imageDrawable)
                        base64Image = encodeToBase64(bitmap)
                    }

                    val updatedExercise = Exercise(
                        name = name,
                        description = description,
                        imageBase64 = base64Image
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

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton?.setTextColor(resources.getColor(R.color.primario))
            negativeButton?.setTextColor(resources.getColor(R.color.primario))
        }

// Devolvemos el diálogo
        return dialog

    }

    // Abre la cámara y guarda la foto en la galería
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

    // Convierte un Bitmap a Base64
    private fun encodeToBase64(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        // Puedes usar JPEG/PNG, y ajustar la calidad
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    // Convierte un Drawable a Bitmap
    private fun drawableToBitmap(drawable: android.graphics.drawable.Drawable): Bitmap? {
        return when (drawable) {
            is android.graphics.drawable.BitmapDrawable -> {
                drawable.bitmap
            }
            else -> {
                // Para otros tipos de Drawable, creamos un bitmap temporal
                val width = drawable.intrinsicWidth
                val height = drawable.intrinsicHeight
                if (width <= 0 || height <= 0) {
                    return null
                }
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = android.graphics.Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
