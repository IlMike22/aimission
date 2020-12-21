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

    fun onGoalStatusChangedError(
            goal: Goal?,
            position: Int
    )

    fun onIterativeGoalsReceived(goals: ArrayList<Goal>)

    fun onDoneGoalsReceived(goals: ArrayList<Goal>)

    fun onIterativeGoalsReceivedError(msg: String)

    fun onHighPriorityGoalsReceived(goals: ArrayList<Goal>)

    fun onHighPriorityGoalsReceivedError(msg: String)

    fun onItemInformationFromSharedPrefSuccess(
            goalsDone: Int,
            goalsHighPriority: Int,
            goalsIterative: Int
    )

    fun onSPStoreSuccess(
            result: Map<String,
                    Int>
    )

    fun onSharedPreferencesStoredError(errorMsg: String)

    fun onItemInformationFromSharedPrefError(msg: String)
}