package com.example.michl.aimission.GoalDetailScene.Views

import com.example.michl.aimission.Models.Goal

interface IGoalDetailFragment {
    fun afterDeleteItemSucceed(msg: String)
    fun afterDeleteItemFailed(msg: String)
    fun onFirebaseUserNotExists(msg: String)
    fun onFirebaseUserExists(userId: String)
    fun afterSaveItemSucceed()
    fun afterSaveItemFailed(msg: String)
    fun afterUpdateItemSucceed(msg: String)
    fun afterUpdateItemFailed(msg: String)
    fun showAimDetailData(item: Goal)
    fun showErrorMessageToUser(msg: String)
    fun afterValidationFailed(message:String)
}