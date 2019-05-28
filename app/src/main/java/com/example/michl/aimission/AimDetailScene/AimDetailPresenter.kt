package com.example.michl.aimission.AimDetailScene

import android.util.Log
import com.example.michl.aimission.AimDetailScene.Views.AimDetailFragmentInput
import com.example.michl.aimission.Models.AimItem
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
    fun onAimReadSuccessfully(item: AimItem)
    fun onErrorMessageCreated(msg: String)
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

    override fun onAimReadSuccessfully(item: AimItem) {
        output?.get()?.showAimDetailData(item)
    }

    override fun onErrorMessageCreated(msg: String) {
        output?.get()?.showErrorMessageToUser(msg)
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

