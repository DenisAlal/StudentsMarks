package com.denisal.studentsmarks.db.tasks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Insert
    fun insertTasks(tasks: TasksDataRoom)
    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TasksDataRoom>>
    @Query("DELETE FROM tasks")
    fun deleteTasks()
    @Query("DELETE FROM tasks WHERE id = :id")
    fun deleteOneTask(id : Int):Int
    @Query("DELETE FROM sqlite_sequence WHERE name = 'tasks';")
    fun resetAutoIncrementValueTasks()
    @Query("SELECT * FROM tasks")
    fun loadAllTasks(): MutableList<TasksDataRoom>
}