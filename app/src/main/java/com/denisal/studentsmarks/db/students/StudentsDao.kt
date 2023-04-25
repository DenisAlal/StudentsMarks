package com.denisal.studentsmarks.db.students

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentsDao {
    @Insert
    fun insertStudent(students: StudentsDataRoom)
    @Query("SELECT * FROM students")
    fun getStudents(): Flow<List<StudentsDataRoom>>
    @Query("DELETE FROM students")
    fun deleteStudents()
    @Query("DELETE FROM students WHERE idStud = :id")
    fun deleteOneStudent(id : Int):Int
    @Query("DELETE FROM sqlite_sequence WHERE name = 'students';")
    fun resetAutoIncrementValueStudents()
    @Query("SELECT * FROM students")
    fun loadAllStudents(): MutableList<StudentsDataRoom?>?
    @Query("SELECT * FROM students")
    fun loadAllStudentsArray(): MutableList<StudentsDataRoom>
}