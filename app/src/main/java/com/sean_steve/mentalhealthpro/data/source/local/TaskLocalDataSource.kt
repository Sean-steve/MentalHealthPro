package com.sean_steve.mentalhealthpro.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.sean_steve.mentalhealthpro.data.Tasks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.sean_steve.mentalhealthpro.data.Results.Success
import com.sean_steve.mentalhealthpro.data.Results
import com.sean_steve.mentalhealthpro.data.source.remote.TaskDataSource

/**
 * Concrete implementation of a data source as a db.
 */
class TaskLocalDataSource internal constructor(
    private val tasksDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskDataSource {

    override fun observeTasks(): LiveData<Results<List<Tasks>>> {
        return tasksDao.observeTasks().map {
            Success(it)
        }
    }

    override fun observeTask(taskId: String): LiveData<Results<Tasks>> {
        return tasksDao.observeTaskById(taskId).map {
            Success(it)
        }
    }

    override suspend fun refreshTask(taskId: String) {
        //NO-OP
    }

    override suspend fun refreshTasks() {
        //NO-OP
    }

    override suspend fun getTasks(): Results<List<Tasks>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(tasksDao.getTasks())
        } catch (e: Exception) {
            Error(e)
        }
    } as Results<List<Tasks>>

    override suspend fun getTask(taskId: String): Results<Tasks> = withContext(ioDispatcher) {
        try {
            val task = tasksDao.getTaskById(taskId)
            if (task != null) {
                return@withContext Success(task)
            } else {
                return@withContext Error(Exception("Task not found!"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    } as Results<Tasks>

    override suspend fun saveTask(task: Tasks) = withContext(ioDispatcher) {
        tasksDao.insertTask(task)
    }

    override suspend fun completeTask(task: Tasks) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id, true)
    }

    override suspend fun completeTask(taskId: String) {
        tasksDao.updateCompleted(taskId, true)
    }

    override suspend fun activateTask(task: Tasks) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id, false)
    }

    override suspend fun activateTask(taskId: String) {
        tasksDao.updateCompleted(taskId, false)
    }

    override suspend fun clearCompletedTasks() = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteCompletedTasks()
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher) {
        tasksDao.deleteTasks()
    }

    override suspend fun deleteTask(taskId: String) = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteTaskById(taskId)
    }
}
