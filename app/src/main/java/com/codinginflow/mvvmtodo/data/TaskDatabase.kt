package com.codinginflow.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class],version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao

    class Callback @Inject constructor(private val database : Provider<TaskDatabase>,private val applicationScope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Do cleaning"))
                dao.insert(Task("Create a new project in AS"))
                dao.insert(Task("Go for a walk with dog"))
                dao.insert(Task("Wash the dishes",isImportant = true))
            }
        }
    }
}