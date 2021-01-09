package com.example.michl.aimission.goalScene

import com.example.michl.aimission.models.Goal

interface IGoalFragment {
    fun afterRemoveItemSuccess(message: String)

    fun afterRemoveItemError(message: String)

    fun onFirebaseUserNotExists(message: String)

    fun onFirebaseUserExists(userId: String)

    fun afterSaveItemSuccess()

    fun afterSaveItemError(message: String)

    fun afterUpdateItemSuccess(message: String)

    fun afterUpdateItemError(message: String)

    fun showGoal(goal: Goal)

    fun showErrorMessageToUser(message: String)

    fun afterValidationError(message:String)

}