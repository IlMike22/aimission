package com.example.michl.aimission.Utility

import com.example.michl.aimission.Models.DefaultSortMode
import com.example.michl.aimission.Models.Goal

class GoalHelper {
    companion object {
        fun sortGoalsBySortMode(
                sortMode: DefaultSortMode,
                goals: ArrayList<Goal>
        ): ArrayList<Goal> {
            when (sortMode) {
                DefaultSortMode.SORT_MODE_CREATION_DATE -> {
                    goals.sortByDescending { goal ->
                        goal.creationDate
                    }
                }
                DefaultSortMode.SORT_MODE_ITEMS_DONE -> {
                    goals.sortByDescending { goal ->
                        goal.status
                    }
                }
                DefaultSortMode.SORT_MODE_PRIORITY -> {
                    goals.sortByDescending { goal ->
                        goal.isHighPriority
                    }
                }
                DefaultSortMode.SORT_MODE_UNKNOWN -> {
                    goals.sortByDescending { goal ->
                        goal.creationDate
                    }
                }
            }
            return goals
        }
    }
}