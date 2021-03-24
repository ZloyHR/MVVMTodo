package com.codinginflow.mvvmtodo.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.data.TaskDao
import com.codinginflow.mvvmtodo.ui.ACTION_ADD_OK
import com.codinginflow.mvvmtodo.ui.ACTION_EDIT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    val taskDao: TaskDao,
    @Assisted private val state : SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")
    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()


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

    fun onEditClick(task: Task) = viewModelScope.launch {
        val newTask = task.copy(name = taskName,isImportant = taskImportant)
        taskDao.update(newTask)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavBack(ACTION_EDIT_OK))
    }

    fun onAddClick() = viewModelScope.launch {
        taskDao.insert(Task(taskName,taskImportant))
        addEditTaskEventChannel.send(AddEditTaskEvent.NavBack(ACTION_ADD_OK))
    }

    fun onTextChanged(text: String) = viewModelScope.launch {
        taskName = text
    }

    fun onCheckBoxChanged(important: Boolean) = viewModelScope.launch {
        taskImportant = important
    }

    fun onSaveTaskClick() {
        if (taskName.isBlank()) {
            viewModelScope.launch{
                addEditTaskEventChannel.send(AddEditTaskEvent.TaskNameEmpty("Task cannot be empty"))
            }
            return
        }

        if(task == null)onAddClick()
        else onEditClick(task)
    }
}

sealed class AddEditTaskEvent{
    data class TaskNameEmpty(val msg: String) : AddEditTaskEvent()
    data class NavBack(val res: Int) : AddEditTaskEvent()
}