package com.sean_steve.mentalhealthpro.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sean_steve.mentalhealthpro.data.Results
import com.sean_steve.mentalhealthpro.data.Tasks


/**
 * Main entry point for accessing tasks data.
 */
interface TaskDataSource {


    fun observeTasks(): LiveData<Results<List<Tasks>>>

    suspend fun getTasks(): Results<List<Tasks>>?

    suspend fun refreshTasks()

    fun observeTask(taskId: String): LiveData<Results<Tasks>>

    suspend fun getTask(taskId: String): Results<Tasks>

    suspend fun refreshTask(taskId: String)

    suspend fun saveTask(task: Tasks)

    suspend fun completeTask(task: Tasks)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Tasks)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}
