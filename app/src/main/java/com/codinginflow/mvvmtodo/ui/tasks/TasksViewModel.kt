package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.codinginflow.mvvmtodo.data.PreferencesManager
import com.codinginflow.mvvmtodo.data.SortOrder
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.data.TaskDao
import com.codinginflow.mvvmtodo.ui.ACTION_ADD_OK
import com.codinginflow.mvvmtodo.ui.ACTION_EDIT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state : SavedStateHandle
    ) : ViewModel() {

    val query = state.getLiveData("query","")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val taskFlow = combine(query.asFlow(),preferencesFlow){
        query,preferencesFlow->
        Pair(query,preferencesFlow)
    } .flatMapLatest {(query,preferencesFlow) ->
        taskDao.getTasks(query,preferencesFlow.sortOrder,preferencesFlow.hideCompleted)
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    val tasks = taskFlow.asLiveData()

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task: Task, checked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(isDone = checked))
    }


    private val tasksEventChannel = Channel<TasksEvent>()
    val taskEvent = tasksEventChannel.receiveAsFlow()

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onTaskUndoClicked(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClicked() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(res: Int) = viewModelScope.launch {
        when(res){
            ACTION_ADD_OK -> tasksEventChannel.send(TasksEvent.ShowSavedMessage("Task added"))
            ACTION_EDIT_OK -> tasksEventChannel.send(TasksEvent.ShowSavedMessage("Task saved"))
        }
    }

    sealed class TasksEvent(){
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowSavedMessage(val msg: String) : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        object NavigateToAddTaskScreen : TasksEvent()
    }

}
