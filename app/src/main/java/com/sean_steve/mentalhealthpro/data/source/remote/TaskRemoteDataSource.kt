package com.sean_steve.mentalhealthpro.data.source.remote

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.sean_steve.mentalhealthpro.data.Results
import com.sean_steve.mentalhealthpro.data.Tasks
import kotlinx.coroutines.delay
import com.sean_steve.mentalhealthpro.data.Results.*

/**
 * Implementation of the data source that adds a latency simulating network.
 */
object TaskRemoteDataSource : TaskDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private var TASKS_SERVICE_DATA = LinkedHashMap<String, Tasks>(2)

    init {
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private val observableTasks = MutableLiveData<Results<List<Tasks>>>()

    @SuppressLint("NullSafeMutableLiveData")
    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override fun observeTasks(): LiveData<Results<List<Tasks>>> {
        return observableTasks
    }

    override fun observeTask(taskId: String): LiveData<Results<Tasks>> {
        return observableTasks.map { tasks ->
            when (tasks) {
                is Results.Loading -> Results.Loading
                is Error -> Error(tasks.exception)
                is Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId }
                        ?: return@map Error(Exception("Not found"))
                    Success(task)
                }
            }
        }
    }

    override suspend fun getTasks(): Results<List<Tasks>>? {
        // Simulate network by delaying the execution.
        val tasks = TASKS_SERVICE_DATA.values.toList()
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Success(tasks)
    }

    override suspend fun getTask(taskId: String): Results<Tasks> {
        // Simulate network by delaying the execution.
        delay(SERVICE_LATENCY_IN_MILLIS)
        TASKS_SERVICE_DATA[taskId]?.let {
            return Success(it)
        }
        return Error(Exception("Task not found"))
    }

    private fun addTask(title: String, description: String) {
        val newTask = Tasks(title, description)
        TASKS_SERVICE_DATA[newTask.id] = newTask
    }

    override suspend fun saveTask(task: Tasks) {
        TASKS_SERVICE_DATA[task.id] = task
    }

    override suspend fun completeTask(task: Tasks) {
        val completedTask = Tasks(task.title, task.description, true, task.id)
        TASKS_SERVICE_DATA[task.id] = completedTask
    }

    override suspend fun completeTask(taskId: String) {
        // Not required for the remote data source
    }

    override suspend fun activateTask(task: Tasks) {
        val activeTask = Tasks(task.title, task.description, false, task.id)
        TASKS_SERVICE_DATA[task.id] = activeTask
    }

    override suspend fun activateTask(taskId: String) {
        // Not required for the remote data source
    }

    override suspend fun clearCompletedTasks() {
        TASKS_SERVICE_DATA = TASKS_SERVICE_DATA.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Tasks>
    }

    override suspend fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    override suspend fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }
}