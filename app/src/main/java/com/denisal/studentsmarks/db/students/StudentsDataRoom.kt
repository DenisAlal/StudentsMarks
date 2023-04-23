package com.denisal.studentsmarks.db.students

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentsDataRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "idStud")
    var idStud: Int,
    @ColumnInfo(name = "group")
    var group: String,
    @ColumnInfo(name = "fio")
    var fio: String,
)
