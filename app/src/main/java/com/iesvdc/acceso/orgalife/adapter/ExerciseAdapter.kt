package com.iesvdc.acceso.orgalife.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.data.Exercise

// ExerciseAdapter
class ExerciseAdapter(
    private val exercises: MutableList<Exercise>,
    private val onDeleteClicked: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.nameTextView.text = exercise.name
        holder.descriptionTextView.text = exercise.description
        holder.imageView.setImageResource(exercise.imageResId)

        // Acción del botón "Eliminar"
        holder.deleteButton.setOnClickListener {
            // Llamamos a la función de eliminación cuando el botón es clickeado
            onDeleteClicked(exercise)
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageExercise)
        val nameTextView: TextView = itemView.findViewById(R.id.textExerciseName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textExerciseDescription)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)  // Cambiado a ImageButton
    }
}
