package com.denisal.studentsmarks.db.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    fun insertSession(lesson: SessionData)
    @Query("SELECT * FROM session")
    fun getSession(): Flow<List<SessionData>>
    @Query("DELETE FROM session")
    fun deleteSession()
    @Query("DELETE FROM session WHERE id = :id")
    fun deleteOneRow(id : Int):Int
    @Query("DELETE FROM sqlite_sequence WHERE name = 'session';")
    fun resetAutoIncrementValueSession()
    @Query("SELECT * FROM session")
    fun loadAllLesson(): MutableList<SessionData?>?
    @Query("SELECT * FROM session")
    fun loadOneLesson() : MutableList<SessionData>
}