package com.sean_steve.mentalhealthpro.data.source.remote

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.sean_steve.mentalhealthpro.data.Results
import com.sean_steve.mentalhealthpro.data.Results.Success
import com.sean_steve.mentalhealthpro.data.Tasks
import com.sean_steve.mentalhealthpro.data.source.local.TaskLocalDataSource
import com.sean_steve.mentalhealthpro.data.source.local.ToDoDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
class DefaultTasksRepository private constructor(application: Application) {

    private val tasksRemoteDataSource: TaskDataSource
    private val tasksLocalDataSource: TaskDataSource
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {
        @Volatile
        private var INSTANCE: DefaultTasksRepository? = null

        fun getRepository(app: Application): DefaultTasksRepository {
            return INSTANCE ?: synchronized(this) {
                DefaultTasksRepository(app).also {
                    INSTANCE = it
                }
            }
        }
    }

    init {
        val database = Room.databaseBuilder(application.applicationContext,
            ToDoDatabase::class.java, "Tasks.db")
            .build()

        tasksRemoteDataSource = TaskRemoteDataSource
        tasksLocalDataSource = TaskLocalDataSource(database.taskDao())
    }

    suspend fun getTasks(forceUpdate: Boolean = false): Results.Error {
        if (forceUpdate) {
            try {
                updateTasksFromRemoteDataSource()
            } catch (ex: Exception) {
                return Results.Error(ex)
            }
        }
        return tasksLocalDataSource.getTasks() as Results.Error
    }

    suspend fun refreshTasks() {
        updateTasksFromRemoteDataSource()
    }

    fun observeTasks(): LiveData<Results<List<Tasks>>> {
        return this.tasksLocalDataSource.observeTasks()
    }

    suspend fun refreshTask(taskId: String) {
        updateTaskFromRemoteDataSource(taskId)
    }

    private suspend fun updateTasksFromRemoteDataSource() {
        val remoteTasks = tasksRemoteDataSource.getTasks()

        if (remoteTasks is Success) {
            // Real apps might want to do a proper sync.
            tasksLocalDataSource.deleteAllTasks()
            remoteTasks.data.forEach { task ->
                tasksLocalDataSource.saveTask(task)
            }
        } else if (remoteTasks is Results.Error) {
            throw remoteTasks.exception
        }
    }

    fun observeTask(taskId: String): LiveData<Results<Tasks>> {
        return tasksLocalDataSource.observeTask(taskId)
    }

    private suspend fun updateTaskFromRemoteDataSource(taskId: String) {
        val remoteTask = tasksRemoteDataSource.getTask(taskId)

        if (remoteTask is Success) {
            tasksLocalDataSource.saveTask(remoteTask.data)
        }
    }

    /**
     * Relies on [getTasks] to fetch data and picks the task with the same ID.
     */
    suspend fun getTask(taskId: String,  forceUpdate: Boolean = false): Results<Tasks> {
        if (forceUpdate) {
            updateTaskFromRemoteDataSource(taskId)
        }
        return tasksLocalDataSource.getTask(taskId)
    }

    suspend fun saveTask(task: Tasks) {
        coroutineScope {
            launch { tasksRemoteDataSource.saveTask(task) }
            launch { tasksLocalDataSource.saveTask(task) }
        }
    }

    suspend fun completeTask(task: Tasks) {
        coroutineScope {
            launch { tasksRemoteDataSource.completeTask(task) }
            launch { tasksLocalDataSource.completeTask(task) }
        }
    }

    suspend fun completeTask(taskId: String) {
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Success)?.let { it ->
                completeTask(it.data)
            }
        }
    }

    suspend fun activateTask(task: Tasks) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { tasksRemoteDataSource.activateTask(task) }
            launch { tasksLocalDataSource.activateTask(task) }
        }
    }

    suspend fun activateTask(taskId: String) {
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Success)?.let { it ->
                activateTask(it.data)
            }
        }
    }

    suspend fun clearCompletedTasks() {
        coroutineScope {
            launch { tasksRemoteDataSource.clearCompletedTasks() }
            launch { tasksLocalDataSource.clearCompletedTasks() }
        }
    }

    suspend fun deleteAllTasks() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { tasksRemoteDataSource.deleteAllTasks() }
                launch { tasksLocalDataSource.deleteAllTasks() }
            }
        }
    }

    suspend fun deleteTask(taskId: String) {
        coroutineScope {
            launch { tasksRemoteDataSource.deleteTask(taskId) }
            launch { tasksLocalDataSource.deleteTask(taskId) }
        }
    }

    private suspend fun getTaskWithId(id: String): Results<Tasks> {
        return tasksLocalDataSource.getTask(id)
    }
}
