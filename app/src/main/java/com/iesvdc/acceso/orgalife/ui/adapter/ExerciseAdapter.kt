package com.iesvdc.acceso.orgalife.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            binding.imageExercise.setImageResource(exercise.imageResId)

            // Lambdas que vienen de la Activity
            binding.deleteButton.setOnClickListener { onDeleteClicked(exercise) }
            binding.editButton.setOnClickListener { onEditClicked(exercise) }
        }
    }
}
