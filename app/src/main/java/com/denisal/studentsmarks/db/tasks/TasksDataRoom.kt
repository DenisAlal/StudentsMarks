package com.denisal.studentsmarks.db.tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TasksDataRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "idTask")
    var idTask: Int,
    @ColumnInfo(name = "taskName")
    var taskName: String,
    @ColumnInfo(name = "courseName")
    var courseName: String,
)
