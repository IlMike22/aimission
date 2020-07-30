package com.example.michl.aimission.GoalScene

import com.example.michl.aimission.Models.Goal

interface IGoalInteractor {
    fun createGoal(userId: String, goal: Goal, isGoalRepeatable:Boolean = false)
    fun updateGoal(userId: String, goal: Goal, isGoalRepeatable: Boolean = false)
    fun deleteGoal(userId: String, goalId: String)
    fun getAndValidateFirebaseUser()
    fun getDetailData(id: String)
    fun createErrorMessageIfItemIdIsNull(msg: String)
}