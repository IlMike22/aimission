package com.example.michl.aimission.AimListScene

import android.util.Log
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

interface AimListPresenterInput {
    fun onItemsLoaded(items: ArrayList<Goal>, month: Int, year: Int)
    fun onNoUserIdExists()
    fun onItemStatusChanged(item: Goal, position: Int)
    fun onItemStatusChangeFailed(item: Goal?, position: Int)
    fun onIterativeItemsGot(items: ArrayList<Goal>)
    fun onCompletedItemsGot(items: ArrayList<Goal>)
    fun onIterativeItemsGotFailed(msg: String)
    fun onHighPriorityItemsGot(items: ArrayList<Goal>)
    fun onHighPriorityItemsGotFailed(msg: String)
    fun onItemInformationFromSharedPrefSucceed(itemsDone: Int, itemsHighPrio: Int, itemsIterative: Int)
    fun onSPStoreSucceed(result: Map<String, Int>)
    fun onSPStoreFailed(errorMsg: String)
    fun onItemInformationFromSharedPrefFailed(msg: String)

}

class AimListPresenter : AimListPresenterInput {
    var output: WeakReference<IGoalsFragment>? = null

    override fun onNoUserIdExists() {
        val msgUserNotFound = "Cannot authenticate current user. Are you already logged in?"
        output?.get()?.afterUserIdNotFound(msgUserNotFound)
    }

    override fun onItemsLoaded(goals: ArrayList<Goal>, month: Int, year: Int) {
        Aimission.getAppContext()?.apply {
            if (goals.isEmpty())
                output?.get()?.afterNoGoalsFound(getString(R.string.goal_list_msg_no_items_for_this_month))
            else
                output?.get()?.afterGoalsLoaded(sortGoals(goals), month, year)
        } ?: Log.e(TAG, "No app context available")

    }

    override fun onItemStatusChanged(item: Goal, position: Int) {
        output?.get()?.afterGoalStatusChange(item, position)
    }

    override fun onItemStatusChangeFailed(item: Goal?, position: Int) {
        item?.let { item ->
            val msg = "Unable to update status from item ${item.title} on position $position."
            Log.e(TAG, msg + " Please try in a few minutes again.")
            output?.get()?.afterGoalStatusChangeFailed(msg)
        } ?: run {
            val msg = "Unable to update status from item. Item is null. Position is $position"
            Log.e(TAG, msg)
            output?.get()?.afterGoalStatusChangeFailed(msg)
        }
    }

    override fun onIterativeItemsGot(items: ArrayList<Goal>) {
        output?.get()?.afterIterativeGoalsLoaded(items)
    }

    override fun onIterativeItemsGotFailed(msg: String) {
        output?.get()?.afterIterativeGoalsLoadedFailed(msg)
    }

    override fun onHighPriorityItemsGot(items: ArrayList<Goal>) {
        output?.get()?.afterHighPriorityGoalsLoaded(items)
    }

    override fun onHighPriorityItemsGotFailed(msg: String) {
        output?.get()?.afterHighPriorityGoalsLoadedFailed(msg)
    }

    override fun onCompletedItemsGot(items: ArrayList<Goal>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemInformationFromSharedPrefSucceed(completedItems: Int, highPrioItems: Int, iterativeItems: Int) {
        val msgCompletedItems = "Currently you have $completedItems items completed successfully. Congrats!"
        val msgHighPrioItemAmount = "In this month you have actually $highPrioItems high priority items."
        val msgIterativeItemAmount = "You have $iterativeItems iterative items in this month."

        output?.get()?.afterGoalInformationLoaded(msgCompletedItems, msgHighPrioItemAmount, msgIterativeItemAmount)
    }

    override fun onItemInformationFromSharedPrefFailed(msg: String) {
        val message = "An error occured while trying to get all the item information from shared pref. Details: $msg"
        output?.get()?.afterGoalInformationLoadedFailed(message)
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

    private fun sortGoals(goals: ArrayList<Goal>): ArrayList<Goal> {
        goals.sortByDescending { goal ->
            goal.creationDate
        }
        return goals
    }
}
