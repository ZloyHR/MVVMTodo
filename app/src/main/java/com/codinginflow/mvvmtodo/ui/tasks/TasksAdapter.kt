package com.codinginflow.mvvmtodo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding

class TasksAdapter : ListAdapter<Task, TasksAdapter.TasksViewHolder>(DiffCallback()) {

    class TasksViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(task: Task){
            binding.apply {
                cbTaskDone.isChecked = task.isDone
                tvTaskName.text = task.name
                tvTaskName.paint.isStrikeThruText = task.isDone
                ivTaskImportant.isVisible = task.isImportant
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}