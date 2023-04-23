package com.denisal.studentsmarks.db.tasks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [TasksDataRoom::class], version = 1)
abstract  class TasksDB: RoomDatabase() {
    abstract fun getTasksDao(): TasksDao
    companion object {
        fun getDB(context: Context): TasksDB {
            return Room.databaseBuilder(
                context.applicationContext,
                TasksDB::class.java,
                "tasksData"
            ).build()
        }
    }
}