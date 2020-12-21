package com.example.michl.aimission.goalScene

import com.example.michl.aimission.models.Goal

interface IGoalFragment {
    fun afterRemoveItemSuccess(msg: String)

    fun afterRemoveItemError(msg: String)

    fun onFirebaseUserNotExists(msg: String)

    fun onFirebaseUserExists(userId: String)

    fun afterSaveItemSuccess()

    fun afterSaveItemError(msg: String)

    fun afterUpdateItemSuccess(msg: String)

    fun afterUpdateItemError(msg: String)

    fun showGoal(item: Goal)

    fun showErrorMessageToUser(msg: String)

    fun afterValidationError(message:String)

}