
package com.sean_steve.mentalhealthpro.statistics

import com.sean_steve.mentalhealthpro.data.Tasks

/**
 * Function that does some trivial computation. Used to showcase unit tests.
 */
internal fun getActiveAndCompletedStats(tasks: List<Tasks>?): StatsResult {
    val totalTasks = tasks!!.size
    val numberOfActiveTasks = tasks.count { it.isActive }
    return StatsResult(
        activeTasksPercent = 100f * numberOfActiveTasks / tasks.size,
        completedTasksPercent = 100f * (totalTasks - numberOfActiveTasks) / tasks.size
    )
}

data class StatsResult(val activeTasksPercent: Float, val completedTasksPercent: Float)
