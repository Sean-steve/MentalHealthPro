
package com.sean_steve.mentalhealthpro.statistics

import android.app.Application
import androidx.lifecycle.*
import com.sean_steve.mentalhealthpro.data.Tasks
import kotlinx.coroutines.launch
import com.sean_steve.mentalhealthpro.data.Results
import com.sean_steve.mentalhealthpro.data.Results.Success
import com.sean_steve.mentalhealthpro.data.source.remote.DefaultTasksRepository


/**
 * ViewModel for the statistics screen.
 */
class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    // Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
    private val tasksRepository = DefaultTasksRepository.getRepository(application)

    private val tasks: LiveData<Results<List<Tasks>>> = tasksRepository.observeTasks()
    private val _dataLoading = MutableLiveData<Boolean>(false)
    private val stats: LiveData<StatsResult?> = tasks.map {
        if (it is Success<*>) {
            getActiveAndCompletedStats(it.data as List<Tasks>?)
        } else {
            null
        }
    }

    val activeTasksPercent = stats.map {
        it?.activeTasksPercent ?: 0f }
    val completedTasksPercent: LiveData<Float> = stats.map { it?.completedTasksPercent ?: 0f }
    val dataLoading: LiveData<Boolean> = _dataLoading
    val error: LiveData<Boolean> = tasks.map { it is Error }
    val empty: LiveData<Boolean> = tasks.map { (it as? Success)?.data.isNullOrEmpty() }

    fun refresh() {
        _dataLoading.value = true
        viewModelScope.launch {
            tasksRepository.refreshTasks()
            _dataLoading.value = false
        }
    }
}
