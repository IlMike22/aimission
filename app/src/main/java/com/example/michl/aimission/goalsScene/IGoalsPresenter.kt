package com.example.michl.aimission.goalsScene

import com.example.michl.aimission.models.Goal

interface IGoalsPresenter {
    fun onGoalsLoaded(
            goals: ArrayList<Goal>,
            month: Int,
            year: Int,
            addedDefaultGoalsSize:Int = 0
    )
    fun onNoUserIdExists()
    fun onGoalStatusChanged(position: Int)
    fun onGoalStatusChangedFailed(
            goal: Goal?,
            position: Int
    )
    fun onIterativeGoalsReceived(goals: ArrayList<Goal>)
    fun onDoneGoalsReceived(goals: ArrayList<Goal>)
    fun onIterativeGoalsReceivedFailed(msg: String)
    fun onHighPriorityGoalsReceived(goals: ArrayList<Goal>)
    fun onHighPriorityGoalsReceivedFailed(msg: String)
    fun onItemInformationFromSharedPrefSucceed(
            goalsDone: Int,
            goalsHighPriority: Int,
            goalsIterative: Int
    )
    fun onSPStoreSucceed(
            result: Map<String,
                    Int>
    )
    fun onSharedPreferencesStoredFailed(errorMsg: String)
    fun onItemInformationFromSharedPrefFailed(msg: String)
}