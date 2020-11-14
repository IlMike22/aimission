package com.example.michl.aimission.goalScene.implementation

import android.util.Log
import com.example.michl.aimission.goalScene.IGoalPresenter
import com.example.michl.aimission.goalScene.IGoalFragment
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

class GoalPresenter : IGoalPresenter {
    var output: WeakReference<IGoalFragment>? = null

    override fun validateFirebaseUser(userID: String) {
        if (userID.isNullOrEmpty())
            output?.get()?.onFirebaseUserNotExists("Firebase ID not found. Are you logged in? Otherwise there cannot be created a new aim")
        else
            output?.get()?.onFirebaseUserExists(userID)
    }

    override fun onStoreGoalSucceed() {
        output?.get()?.afterSaveItemSucceed()
    }

    override fun onStoreGoalFailed() {
        val msg = "An error occured while trying to update item."
        output?.get()?.afterSaveItemFailed(msg)
    }

    override fun onGoalReadSucceed(goal: Goal) {
        output?.get()?.showGoal(goal)
    }

    override fun onErrorMessageCreated(msg: String) {
        output?.get()?.showErrorMessageToUser(msg)
    }

    override fun showValidationError(result: ValidationResult) {
        val message = when (result) {
            ValidationResult.NO_STATUS_DEFINED_ERROR -> "Item cannot be stored since there is a no status defined."
            ValidationResult.NO_GENRE_DEFINED_ERROR -> "Please define a genre before you save this goal."
            ValidationResult.ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR -> "Please define title and description for this goal."
            ValidationResult.NO_AMOUNT_OF_REPEATS_ERROR -> "Your goal is repeatable. Please set the amount of repeats for this goal."
            else -> "Oha an unknown validation error occured. Cannot store this item. Please try again later."
        }
        output?.get()?.afterValidationFailed(message)
    }

    override fun onUpdateGoalFailed() {
        val msg = "An error occured while trying to update item."
        Log.e(TAG, msg)
        output?.get()?.afterUpdateItemFailed(msg)
    }

    override fun updateGoalSucceed() {
        val msg = "Item was updated successfully."
        Log.i(TAG, msg)
        output?.get()?.afterUpdateItemSucceed(msg)
    }

    override fun onDeleteGoalSucceed() {
        val msg = "Item was deleted successfully."
        output?.get()?.afterDeleteItemSucceed(msg)
    }

    override fun onDeleteGoalFailed() {
        val msg = "An error occured while trying to delete item."
        output?.get()?.afterDeleteItemFailed(msg)
    }
}

