package com.example.michl.aimission.GoalScene

import com.example.michl.aimission.GoalScene.implementation.ValidationResult
import com.example.michl.aimission.Models.Goal

interface IGoalPresenter {
    fun validateFirebaseUser(userID: String)
    fun onStoreGoalSucceed()
    fun onStoreGoalFailed()
    fun updateGoalSucceed()
    fun onUpdateGoalFailed()
    fun onDeleteGoalSucceed()
    fun onDeleteGoalFailed()
    fun onGoalReadSucceed(goal: Goal)
    fun onErrorMessageCreated(msg: String)
    fun showValidationError(result: ValidationResult)
}