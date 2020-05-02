package com.example.michl.aimission.GoalDetailScene

import com.example.michl.aimission.Models.Goal

interface IGoalInteractor {
    fun createGoal(userId: String, item: Goal)
    fun deleteGoal(userId: String, itemId: String)
    fun updateGoal(userId: String, item: Goal)
    fun getAndValidateFirebaseUser()
    fun getDetailData(id: String)
    fun createErrorMessageIfItemIdIsNull(msg: String)
}