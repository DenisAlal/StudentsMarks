package com.denisal.studentsmarks.viewsgrade.viewmodels

data class GradeDataView(
    val assessmentID: Int,
    val courseName: String,
    val lessonName: String,
    val taskName: String,
    val group: String,
    val fio: String,
    val date: String,
    val value: String,

)
