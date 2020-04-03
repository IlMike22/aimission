package com.example.michl.aimission.GoalsScene

import com.example.michl.aimission.Models.Goal

interface IGoalsFragment {
    fun afterUserIdNotFound(msg: String)
    fun afterGoalsLoaded(goals: ArrayList<Goal>, month: Int, year: Int)
    fun afterGoalsLoadedFailed(errorMsg: String)
    fun afterNoGoalsFound(msg: String)
    fun afterGoalStatusChange(goal: Goal, position: Int)
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