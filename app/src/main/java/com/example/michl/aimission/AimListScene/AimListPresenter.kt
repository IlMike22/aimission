package com.example.michl.aimission.AimListScene

import android.util.Log
import com.example.michl.aimission.AimListScene.Views.AimListFragmentInput
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

interface AimListPresenterInput {
    fun onItemsLoadedSuccessfully(items: ArrayList<AimItem>)
    fun onNoUserIdExists()
    fun onItemStatusChanged(item: AimItem)
    fun onItemStatusChangeFailed(item: AimItem?)
    fun onIterativeItemsGot(items:ArrayList<AimItem>)
    fun onCompletedItemsGot(items:ArrayList<AimItem>)
    fun onIterativeItemsGotFailed(msg:String)
    fun onHighPriorityItemsGot(items:ArrayList<AimItem>)
    fun onHighPriorityItemsGotFailed(msg:String)
}

class AimListPresenter : AimListPresenterInput {



    var output: WeakReference<AimListFragmentInput>? = null

    override fun onNoUserIdExists() {
        val msgUserNotFound = "Cannot authenticate current user. Are you already logged in?"
        output?.get()?.afterUserIdNotFound(msgUserNotFound)
    }

    override fun onItemsLoadedSuccessfully(items: ArrayList<AimItem>) {
        if (items.size == 0)
            output?.get()?.afterNoUserItemsFound("You have no items defined for this month")
        output?.get()?.afterUserItemsLoadedSuccessfully(items)
    }

    override fun onItemStatusChanged(item: AimItem) {
        output?.get()?.afterItemStatusChangeSucceed(item)
    }

    override fun onItemStatusChangeFailed(item: AimItem?) {

        item?.let { item ->
            val msg = "Unable to update status from item ${item.title}. Please try it again in a few minutes."
            Log.e(TAG, msg)
            output?.get()?.afterItemStatusChangeFailed(msg)
        } ?: run {
            val msg = "Unable to update status from item. Item is null."
            Log.e(TAG, msg)
            output?.get()?.afterItemStatusChangeFailed(msg)
        }
    }

    override fun onIterativeItemsGot(items: ArrayList<AimItem>) {
        output?.get()?.afterIterativeItemsGot(items)
    }

    override fun onIterativeItemsGotFailed(msg: String) {
       output?.get()?.afterIterativeItemsGotFailed(msg)
    }

    override fun onHighPriorityItemsGot(items: ArrayList<AimItem>) {
       output?.get()?.afterHighPriorityItemsGot(items)
    }

    override fun onHighPriorityItemsGotFailed(msg: String) {
        output?.get()?.afterHighPriorityItemsGotFailed(msg)
    }

    override fun onCompletedItemsGot(items: ArrayList<AimItem>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
