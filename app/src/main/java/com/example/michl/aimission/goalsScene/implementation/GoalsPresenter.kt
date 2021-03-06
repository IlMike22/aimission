package com.example.michl.aimission.goalsScene.implementation

import android.util.Log
import com.example.michl.aimission.goalsScene.IGoalsFragment
import com.example.michl.aimission.goalsScene.IGoalsPresenter
import com.example.michl.aimission.models.DefaultSortMode
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.R
import com.example.michl.aimission.settingScene.views.SettingsFragment.Companion.toDefaultSortMode
import com.example.michl.aimission.utitlity.Aimission
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import com.example.michl.aimission.utitlity.GoalHelper.Companion.sortGoalsBySortMode
import com.example.michl.aimission.utitlity.SettingHelper
import java.lang.ref.WeakReference

class GoalsPresenter : IGoalsPresenter {
    var output: WeakReference<IGoalsFragment>? = null

    override fun onNoUserIdExists() {
        val msgUserNotFound = "Cannot authenticate current user. Are you already logged in?"
        output?.get()?.afterUserIdNotFound(msgUserNotFound)
    }

    override fun onGoalsLoaded(
            goals: ArrayList<Goal>,
            month: Int,
            year: Int,
            addedDefaultGoalsSize:Int
    ) {
        Aimission.getAppContext()?.apply {
            if (goals.isEmpty())
                output?.get()?.afterNoGoalsFound(getString(R.string.goal_list_msg_no_items_for_this_month))
            else
                output?.get()?.afterGoalsLoaded(
                        goals = sortGoalsByDefaultSettings(goals),
                        month = month,
                        year = year,
                        addedDefaultGoalsSize = addedDefaultGoalsSize
                )
        }
    }

    override fun onGoalStatusChanged(position: Int) {
        output?.get()?.afterGoalStatusChange(position)
    }

    override fun onGoalStatusChangedError(goal: Goal?, position: Int) {
        goal?.let { item ->
            val msg = "Unable to update status from item ${item.title} on position $position."
            Log.e(TAG, msg + " Please try in a few minutes again.")
            output?.get()?.afterGoalStatusChangeError(msg)
        } ?: run {
            val msg = "Unable to update status from item. Item is null. Position is $position"
            Log.e(TAG, msg)
            output?.get()?.afterGoalStatusChangeError(msg)
        }
    }

    override fun onIterativeGoalsReceived(goals: ArrayList<Goal>) {
        output?.get()?.afterIterativeGoalsLoaded(goals)
    }

    override fun onIterativeGoalsReceivedError(msg: String) {
        output?.get()?.afterIterativeGoalsLoadedError(msg)
    }

    override fun onHighPriorityGoalsReceived(goals: ArrayList<Goal>) {
        output?.get()?.afterHighPriorityGoalsLoaded(goals)
    }

    override fun onHighPriorityGoalsReceivedError(msg: String) {
        output?.get()?.afterHighPriorityGoalsLoadedError(msg)
    }

    override fun onDoneGoalsReceived(goals: ArrayList<Goal>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemInformationFromSharedPrefSuccess(completedItems: Int, highPrioItems: Int, iterativeItems: Int) {
        val msgCompletedItems = "Currently you have $completedItems items completed successfully. Congrats!"
        val msgHighPrioItemAmount = "In this month you have actually $highPrioItems high priority items."
        val msgIterativeItemAmount = "You have $iterativeItems iterative items in this month."

        output?.get()?.afterGoalInformationLoaded(msgCompletedItems, msgHighPrioItemAmount, msgIterativeItemAmount)
    }

    override fun onItemInformationFromSharedPrefError(msg: String) {
        val message = "An error occured while trying to get all the item information from shared pref. Details: $msg"
        output?.get()?.afterGoalInformationLoadedError(message)
    }

    override fun onSPStoreSuccess(result: Map<String, Int>) {
        val itemsDoneMsg = "Currently you have ${result["itemsDone"]} items completed for this month."
        val itemsHighPrioMsg = "There are ${result["itemsHighPrio"]} items with high priority this month."
        val itemsIterativeMsg = "${result["itemsIterative"]} items are iterative."

        output?.get()?.afterSPStoredSuccess(itemsDoneMsg, itemsHighPrioMsg, itemsIterativeMsg)
    }

    override fun onSharedPreferencesStoredError(errorMsg: String) {
        val message = "Something went wrong while trying to store current item status in sp. Details: $errorMsg"
        output?.get()?.afterSPStoredError(message)
    }

    private fun sortGoalsByDefaultSettings(goals: ArrayList<Goal>): ArrayList<Goal> {
        var defaultSortMode = DefaultSortMode.SORT_MODE_CREATION_DATE
        Aimission.getAppContext()?.apply {
            defaultSortMode = SettingHelper.getDefaultSortSetting(this).toDefaultSortMode()
        }

        return sortGoalsBySortMode(defaultSortMode, goals)
    }

    private fun setRepeatCountText(repeatCount:Int, partGoalsAchieved:Int):String = "$partGoalsAchieved von $repeatCount Zielen erreicht"
}
