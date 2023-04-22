package com.denisal.studentsmarks.db.session

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [SessionData::class], version = 1)
abstract  class SessionDB: RoomDatabase() {
    abstract fun getSessionDao(): SessionDao
    companion object {
        fun getDB(context: Context): SessionDB {
            return Room.databaseBuilder(
                context.applicationContext,
                SessionDB::class.java,
                "sessionData"
            ).build()
        }
    }
}