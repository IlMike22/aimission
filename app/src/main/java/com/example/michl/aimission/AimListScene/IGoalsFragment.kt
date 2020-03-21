package com.example.michl.aimission.AimListScene

import com.example.michl.aimission.Models.Goal

interface IGoalsFragment {
    fun afterUserIdNotFound(msg: String)
    fun afterGoalsLoaded(items: ArrayList<Goal>, month: Int, year: Int)
    fun afterGoalsLoadedFailed(errorMsg: String)
    fun afterNoGoalsFound(msg: String)
    fun afterGoalStatusChange(item: Goal, position: Int)
    fun afterGoalStatusChangeFailed(msg: String)
    fun afterIterativeGoalsLoaded(items: ArrayList<Goal>)
    fun afterIterativeGoalsLoadedFailed(msg: String)
    fun afterHighPriorityGoalsLoaded(items: ArrayList<Goal>)
    fun afterHighPriorityGoalsLoadedFailed(msg: String)
    fun afterGoalInformationLoaded(msgItemsCompleted: String, msgItemsHighPrio: String, msgItemsIterative: String)
    fun afterGoalInformationLoadedFailed(errorMsg: String)
    fun afterSPStoredSucceed(itemsDoneMsg: String, itemsHighPrioMsg: String, itemsIterativeMsg: String)
    fun afterSPStoredFailed(message: String)
}