package com.example.michl.aimission.GoalsScene

import com.example.michl.aimission.Models.Goal

interface IGoalsFragment {
    fun afterUserIdNotFound(msg: String)
    fun afterGoalsLoaded(
            goals: ArrayList<Goal>,
            month: Int,
            year: Int,
            addedDefaultGoalsSize: Int = 0
    )
    fun afterGoalsLoadedFailed(errorMsg: String)
    fun afterNoGoalsFound(msg: String)
    fun afterGoalStatusChange(position: Int)
    fun afterGoalStatusChangeFailed(msg: String)
    fun afterIterativeGoalsLoaded(goals: ArrayList<Goal>)
    fun afterIterativeGoalsLoadedFailed(msg: String)
    fun afterHighPriorityGoalsLoaded(goals: ArrayList<Goal>)
    fun afterHighPriorityGoalsLoadedFailed(msg: String)
    fun afterGoalInformationLoaded(msgItemsCompleted: String, msgItemsHighPrio: String, msgItemsIterative: String)
    fun afterGoalInformationLoadedFailed(errorMsg: String)
    fun afterSPStoredSucceed(itemsDoneMsg: String, itemsHighPrioMsg: String, itemsIterativeMsg: String)
    fun afterSPStoredFailed(message: String)
}