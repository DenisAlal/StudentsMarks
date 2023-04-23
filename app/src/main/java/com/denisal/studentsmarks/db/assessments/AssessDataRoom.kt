package com.denisal.studentsmarks.db.assessments

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessment")
data class AssesDataRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "value")
    var value: String,
    @ColumnInfo(name = "taskName")
    var taskName: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "student_id")
    var student_id: Int,
    @ColumnInfo(name = "task_id")
    var task_id: Int,
    )
