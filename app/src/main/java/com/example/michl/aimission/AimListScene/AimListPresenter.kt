package com.example.michl.aimission.AimListScene

import android.util.Log
import com.example.michl.aimission.AimListScene.Views.AimListFragmentInput
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

interface AimListPresenterInput {
    fun onItemsLoadedSuccessfully(items: ArrayList<AimItem>, month: Int, year: Int)
    fun onNoUserIdExists()
    fun onItemStatusChanged(item: AimItem, position: Int)
    fun onItemStatusChangeFailed(item: AimItem?, position: Int)
    fun onIterativeItemsGot(items: ArrayList<AimItem>)
    fun onCompletedItemsGot(items: ArrayList<AimItem>)
    fun onIterativeItemsGotFailed(msg: String)
    fun onHighPriorityItemsGot(items: ArrayList<AimItem>)
    fun onHighPriorityItemsGotFailed(msg: String)
    fun onItemInformationFromSharedPrefSucceed(itemsDone: Int, itemsHighPrio: Int, itemsIterative: Int)
    fun onSPStoreSucceed(result: Map<String, Int>)
    fun onSPStoreFailed(errorMsg: String)
    fun onItemInformationFromSharedPrefFailed(msg: String)

}

class AimListPresenter : AimListPresenterInput {

    var output: WeakReference<AimListFragmentInput>? = null

    override fun onNoUserIdExists() {
        val msgUserNotFound = "Cannot authenticate current user. Are you already logged in?"
        output?.get()?.afterUserIdNotFound(msgUserNotFound)
    }

    override fun onItemsLoadedSuccessfully(items: ArrayList<AimItem>, month: Int, year: Int) {
        if (items.size == 0)
            output?.get()?.afterNoUserItemsFound("You have no items defined for this month")
        else
            output?.get()?.afterUserItemsLoadedSuccessfully(items, month, year)
    }

    override fun onItemStatusChanged(item: AimItem, position: Int) {
        output?.get()?.afterItemStatusChangeSucceed(item, position)
    }

    override fun onItemStatusChangeFailed(item: AimItem?, position: Int) {

        item?.let { item ->
            val msg = "Unable to update status from item ${item.title} on position $position."
            Log.e(TAG, msg + " Please try in a few minutes again.")
            output?.get()?.afterItemStatusChangeFailed(msg)
        } ?: run {
            val msg = "Unable to update status from item. Item is null. Position is $position"
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

    override fun onItemInformationFromSharedPrefSucceed(completedItems: Int, highPrioItems: Int, iterativeItems: Int) {
        val msgCompletedItems = "Currently you have $completedItems items completed successfully. Congrats!"
        val msgHighPrioItemAmount = "In this month you have actually $highPrioItems high priority items."
        val msgIterativeItemAmount = "You have $iterativeItems iterative items in this month."

        output?.get()?.afterItemInformationFromSharedPrefSucceed(msgCompletedItems, msgHighPrioItemAmount, msgIterativeItemAmount)
    }

    override fun onItemInformationFromSharedPrefFailed(msg: String) {
        val message = "An error occured while trying to get all the item information from shared pref. Details: $msg"
        output?.get()?.afterItemInformationFromSharedPrefFailed(message)
    }

    override fun onSPStoreSucceed(result: Map<String, Int>) {
        val itemsDoneMsg = "Currently you have ${result["itemsDone"]} items completed for this month."
        val itemsHighPrioMsg = "There are ${result["itemsHighPrio"]} items with high priority this month."
        val itemsIterativeMsg = "${result["itemsIterative"]} items are iterative."

        output?.get()?.afterSPStoredSucceed(itemsDoneMsg, itemsHighPrioMsg, itemsIterativeMsg)
    }

    override fun onSPStoreFailed(errorMsg: String) {
        val message = "Something went wrong while trying to store current item status in sp. Details: $errorMsg"
        output?.get()?.afterSPStoredFailed(message)
    }


}
