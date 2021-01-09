package com.example.michl.aimission.goalScene.implementation

import android.util.Log
import com.example.michl.aimission.goalScene.IGoalPresenter
import com.example.michl.aimission.goalScene.IGoalFragment
import com.example.michl.aimission.goalScene.implementation.ValidationResult.*
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

class GoalPresenter : IGoalPresenter {
    var output: WeakReference<IGoalFragment>? = null

    override fun validateFirebaseUser(userID: String) {
        if (userID.isEmpty())
            output?.get()?.onFirebaseUserNotExists("Firebase ID not found. Are you logged in? Otherwise there cannot be created a new aim")
        else
            output?.get()?.onFirebaseUserExists(userID)
    }

    override fun onStoreGoalSucceed() {
        output?.get()?.afterSaveItemSuccess()
    }

    override fun onStoreGoalFailed() {
        val msg = "An error occured while trying to update item."
        output?.get()?.afterSaveItemError(msg)
    }

    override fun onGoalReadSucceed(goal: Goal) {
        output?.get()?.showGoal(goal)
    }

    override fun onErrorMessageCreated(message: String) {
        output?.get()?.showErrorMessageToUser(message)
    }

    override fun showValidationError(result: ValidationResult) {
        val message = when (result) {
            NO_STATUS_DEFINED_ERROR -> "Item cannot be stored since there is a no status defined."
            NO_GENRE_DEFINED_ERROR -> "Please define a genre before you save this goal."
            ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR -> "Please define title and description for this goal."
            NO_AMOUNT_OF_REPEATS_ERROR -> "Your goal is repeatable. Please set the amount of repeats for this goal."
            else -> "Oha an unknown validation error occured. Cannot store this item. Please try again later."
        }

        output?.get()?.afterValidationError(message)
    }

    override fun onUpdateGoalFailed() {
        val msg = "An error occured while trying to update item."
        Log.e(TAG, msg)
        output?.get()?.afterUpdateItemError(msg)
    }

    override fun updateGoalSucceed() {
        val msg = "Item was updated successfully."
        Log.i(TAG, msg)
        output?.get()?.afterUpdateItemSuccess(msg)
    }

    override fun onDeleteGoalSucceed() {
        val msg = "Item was deleted successfully."
        output?.get()?.afterRemoveItemSuccess(msg)
    }

    override fun onDeleteGoalFailed() {
        val msg = "An error occured while trying to delete item."
        output?.get()?.afterRemoveItemError(msg)
    }
}

