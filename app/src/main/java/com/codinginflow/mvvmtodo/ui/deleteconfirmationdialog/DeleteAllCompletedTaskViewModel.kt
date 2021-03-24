package com.codinginflow.mvvmtodo.ui.deleteconfirmationdialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.codinginflow.mvvmtodo.data.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val scope: CoroutineScope
): ViewModel() {

    fun onConfirmClick() = scope.launch {
        taskDao.deleteAllCompleted()
    }
}