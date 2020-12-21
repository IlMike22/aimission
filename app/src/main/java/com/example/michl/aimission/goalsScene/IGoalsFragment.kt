package com.example.michl.aimission.goalsScene

import com.example.michl.aimission.models.Goal

interface IGoalsFragment {
    fun afterUserIdNotFound(msg: String)

    fun afterGoalsLoaded(
            goals: ArrayList<Goal>,
            month: Int,
            year: Int,
            addedDefaultGoalsSize: Int = 0
    )
    fun afterGoalsLoadedError(errorMsg: String)

    fun afterNoGoalsFound(msg: String)

    fun afterGoalStatusChange(position: Int)

    fun afterGoalStatusChangeError(msg: String)

    fun afterIterativeGoalsLoaded(goals: ArrayList<Goal>)

    fun afterIterativeGoalsLoadedError(msg: String)

    fun afterHighPriorityGoalsLoaded(goals: ArrayList<Goal>)

    fun afterHighPriorityGoalsLoadedError(msg: String)

    fun afterGoalInformationLoaded(
            msgItemsCompleted: String,
            msgItemsHighPrio: String,
            msgItemsIterative: String
    )

    fun afterGoalInformationLoadedError(errorMsg: String)

    fun afterSPStoredSuccess(
            itemsDoneMsg: String,
            itemsHighPrioMsg: String,
            itemsIterativeMsg: String
    )

    fun afterSPStoredError(message: String)
}