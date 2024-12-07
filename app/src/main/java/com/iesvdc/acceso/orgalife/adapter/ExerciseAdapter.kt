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
import com.iesvdc.acceso.orgalife.databinding.ItemExerciseBinding

// ExerciseAdapter
class ExerciseAdapter(
    private val exercises: List<Exercise>,
    private val onDeleteClicked: (Exercise) -> Unit,
    private val onEditClicked: (Exercise) -> Unit,
    private val onAddClicked: () -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.textExerciseName.text = exercise.name
            binding.textExerciseDescription.text = exercise.description
            binding.imageExercise.setImageResource(exercise.imageResId)

            binding.deleteButton.setOnClickListener {
                onDeleteClicked(exercise)
            }

            binding.editButton.setOnClickListener {
                onEditClicked(exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(ItemExerciseBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}
