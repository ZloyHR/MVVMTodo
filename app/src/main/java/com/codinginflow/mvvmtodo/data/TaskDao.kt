package com.codinginflow.mvvmtodo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    fun getTasks(query : String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getTasksSortedByDate(query,hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query,hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (isDone != :hideCompleted OR isDone == 0) AND name LIKE '%' || :query || '%' ORDER BY isImportant DESC, timeCreated")
    fun getTasksSortedByDate(query : String,hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (isDone != :hideCompleted OR isDone == 0) AND name LIKE '%' || :query || '%' ORDER BY isImportant DESC, name")
    fun getTasksSortedByName(query : String,hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table WHERE isDone == 1")
    suspend fun deleteAllCompleted()
}