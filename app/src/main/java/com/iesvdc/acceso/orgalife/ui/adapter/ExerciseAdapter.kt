package com.iesvdc.acceso.orgalife.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.domain.models.Exercise
import com.iesvdc.acceso.orgalife.databinding.ItemExerciseBinding

class ExerciseAdapter(
    private val onDeleteClicked: (Exercise) -> Unit,
    private val onEditClicked: (Exercise) -> Unit,
    private val onAddClicked: () -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    private val exercises = mutableListOf<Exercise>()

    fun setExercises(newList: List<Exercise>) {
        exercises.clear()
        exercises.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount() = exercises.size

    inner class ViewHolder(private val binding: ItemExerciseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: Exercise) {
            binding.textExerciseName.text = exercise.name
            binding.textExerciseDescription.text = exercise.description

            // Si exercise.imageBase64 no está vacío, decodificamos y cargamos con Glide
            val base64String = exercise.imageBase64
            if (!base64String.isNullOrEmpty()) {
                // Decodificamos la cadena en bytes
                val decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)

                // Cargamos con Glide
                Glide.with(binding.root.context)
                    .asBitmap()           // indicamos que lo interpretaremos como bitmap
                    .load(decodedBytes)   // pasamos el array de bytes
                    .into(binding.imageExercise)
            } else {
                // Si está vacío o null, podrías poner un placeholder
                binding.imageExercise.setImageResource(R.mipmap.ic_icono_principal_foreground)
            }

            // Lambdas que vienen de la Activity
            binding.deleteButton.setOnClickListener { onDeleteClicked(exercise) }
            binding.editButton.setOnClickListener { onEditClicked(exercise) }
        }

    }
}
