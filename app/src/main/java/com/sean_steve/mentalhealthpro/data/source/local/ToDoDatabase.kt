package com.sean_steve.mentalhealthpro.data.source.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.sean_steve.mentalhealthpro.data.Tasks

/**
 * The Room Database that contains the Task table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [Tasks::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}
