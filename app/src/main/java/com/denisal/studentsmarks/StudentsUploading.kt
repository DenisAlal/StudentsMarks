package com.denisal.studentsmarks


data class StudentsData(
    val id: Int,
    val group: String,
    val fullName: String,
)
var filenameDelete = ""
val studData: MutableList<StudentsData> = mutableListOf()
var uid: String = "null"

