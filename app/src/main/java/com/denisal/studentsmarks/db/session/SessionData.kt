package com.denisal.studentsmarks.db.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session")
data class SessionData(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "idCourse")
    var idCourse: Int,
    @ColumnInfo(name = "nameCourse")
    var nameCourse: String,
    @ColumnInfo(name = "nameLesson")
    var nameLesson: String,
    @ColumnInfo(name = "lessonType")
    var lessonType: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "numberSubj")
    var numberSubj: Int
)
