package com.codinginflow.mvvmtodo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
@Entity(tableName = "task_table")
data class Task(val name: String,
                val isImportant: Boolean = false,
                val isDone: Boolean = false,
                val timeCreated: Long = System.currentTimeMillis(),
                @PrimaryKey(autoGenerate = true)
                val id: Int = 0)
    : Parcelable{
    val getTimeFormatted : String
        get() = DateFormat.getDateTimeInstance().format(timeCreated)
}
