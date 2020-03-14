package com.example.michl.aimission.AimDetailScene

import android.util.Log
import com.example.michl.aimission.AimDetailScene.Views.AimDetailFragmentInput
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

interface AimDetailPresenterInput {
    fun validateFirebaseUser(userID: String)
    fun onSaveItemSucceed()
    fun onSaveItemFailed()
    fun onUpdateItemSucceed()
    fun onUpdateItemFailed()
    fun onDeleteItemSucceed()
    fun onDeleteItemFailed()
    fun onAimReadSuccessfully(item: Goal)
    fun onErrorMessageCreated(msg: String)
    fun showValidationError(result:ValidationResult)
}

class AimDetailPresenter : AimDetailPresenterInput {


    var output: WeakReference<AimDetailFragmentInput>? = null

    override fun validateFirebaseUser(userID: String) {
        if (userID.isNullOrEmpty())
            output?.get()?.onFirebaseUserNotExists("Firebase ID not found. Are you logged in? Otherwise there cannot be created a new aim")
        else
            output?.get()?.onFirebaseUserExists(userID)
    }

    override fun onSaveItemSucceed() {
        output?.get()?.afterSaveItemSucceed()
    }

    override fun onSaveItemFailed() {
        val msg = "An error occured while trying to update item."
        output?.get()?.afterSaveItemFailed(msg)
    }

    override fun onAimReadSuccessfully(item: Goal) {
        output?.get()?.showAimDetailData(item)
    }

    override fun onErrorMessageCreated(msg: String) {
        output?.get()?.showErrorMessageToUser(msg)
    }

    override fun showValidationError(result: ValidationResult) {
       val message = when (result){
           ValidationResult.NO_STATUS_DEFINED_ERROR -> "Item cannot be stored since there is a no status defined."
           ValidationResult.NO_GENRE_DEFINED_ERROR -> "Please define a genre before you save this goal."
           ValidationResult.ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR -> "Please define title and description for this goal."
           ValidationResult.NO_AMOUNT_OF_REPEATS_ERROR -> "Your goal is repeatable. Please set the amount of repeats for this goal."
           else -> "Oha an unknown validation error occured. Cannot store this item. Please try again later."
       }
        output?.get()?.afterValidationFailed(message)
    }

    override fun onUpdateItemFailed() {
        val msg = "An error occured while trying to update item."
        Log.e(TAG, msg)
        output?.get()?.afterUpdateItemFailed(msg)
    }

    override fun onUpdateItemSucceed() {
        val msg = "Item was updated successfully."
        Log.i(TAG, msg)
        output?.get()?.afterUpdateItemSucceed(msg)
    }

    override fun onDeleteItemSucceed() {
        val msg = "Item was deleted successfully."
        output?.get()?.afterDeleteItemSucceed(msg)
    }

    override fun onDeleteItemFailed() {
       val msg = "An error occured while trying to delete item."
        output?.get()?.afterDeleteItemFailed(msg)
    }


}

