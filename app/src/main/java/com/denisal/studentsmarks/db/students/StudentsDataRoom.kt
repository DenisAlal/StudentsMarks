package com.denisal.studentsmarks.db.students

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentsDataRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "idStud")
    var idStud: Int,
    @ColumnInfo(name = "fio")
    var fio: String,
    @ColumnInfo(name = "group")
    var group: String,
)
