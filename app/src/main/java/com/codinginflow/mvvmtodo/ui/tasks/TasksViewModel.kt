package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codinginflow.mvvmtodo.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class TasksViewModel @ViewModelInject constructor(private val taskDao: TaskDao) : ViewModel() {

    val query = MutableStateFlow("")

    private val taskFlow = query.flatMapLatest {
        taskDao.getTasks(it)
    }

    val tasks = taskFlow.asLiveData()
}