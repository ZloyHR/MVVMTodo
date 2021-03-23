package com.codinginflow.mvvmtodo.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.data.TaskDao

class AddEditTaskViewModel @ViewModelInject constructor(
    val taskDao: TaskDao,
    @Assisted private val state : SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName",value)
        }
    var taskImportant = state.get<Boolean>("taskImportant") ?: task?.isImportant ?: false
        set(value) {
            field = value
            state.set("taskImportant",value)
        }

}