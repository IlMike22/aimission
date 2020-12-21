package com.example.michl.aimission.goalScene

import com.example.michl.aimission.models.Goal

interface IGoalInteractor {
    fun createGoal(
            userId: String,
            goal: Goal,
            isGoalRepeatable:Boolean = false
    )

    fun updateGoal(
            userId: String,
            goal: Goal,
            isGoalRepeatable: Boolean = false
    )

    fun removeGoal(
            userId: String,
            goalId: String
    )

    fun getAndValidateFirebaseUser()

    fun getDetailData(id: String)

    fun createErrorMessageIfItemIdIsNull(msg: String)

}