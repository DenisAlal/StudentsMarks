package com.denisal.studentsmarks.db.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentsData(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "fio")
    var fio: String,
    @ColumnInfo(name = "group")
    var group: String,
)
