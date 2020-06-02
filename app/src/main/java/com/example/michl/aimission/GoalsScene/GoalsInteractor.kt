package com.example.michl.aimission.GoalsScene

import android.util.Log
import com.example.michl.aimission.Helper.DateHelper.DateHelper.convertDataInGoals
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getGoalTableReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

class GoalsInteractor : IGoalsInteractor {
    var output: IGoalsPresenter? = null
    var goals = ArrayList<Goal>()

    override fun getGoals(
            userId: String,
            data: DataSnapshot,
            monthItem: Month
    ) {
        val userId = getCurrentUserId()

        if (userId.isNullOrEmpty()) {
            output?.onNoUserIdExists()
            return
        }

        if (monthItem.isFirstStart) {
            // get all default goals and create them for new month, set isFirstStart then to false and save this in firebase
            DbHelper.
        }

        goals = getGoalsFromDb(userId, data, monthItem.month, monthItem.year)
        output?.onGoalsLoaded(goals, monthItem.month, monthItem.year)

    }

    override fun changeGoalProgress(goal: Goal?, position: Int) {
        goal?.apply {
            // change progress status
            if (status == Status.DONE)
                status = Status.OPEN
            else if (status == Status.OPEN)
                status = Status.DONE

            // update item list
            goals.get(position).status = status
            output?.onGoalStatusChanged(goal, position)

        } ?: run {
            output?.onGoalStatusChangedFailed(null, position)
        }
    }


    override fun storeGoalsInSharedPreferences(goals: ArrayList<Goal>) {
        // stores current state of item information for this month in sp and returns the result in a dict
        if (goals.size == 0) {
            output?.onSharedPreferencesStoredFailed("No items found.")
            return
        }

        val context = Aimission.getAppContext()
        val month = goals[0].month
        val year = goals[0].year
        val spEntries = getCurrentSPEntry(month, year)

        val itemsCompleted = getGoalsCompleted(goals).size
        val itemsHighPrio = getHighPriorityItems(goals).size
        val itemsIterative = getIterativeItems(goals).size

        // get information about the items and store this information in shared prefs.
        context?.apply {
            DbHelper.storeInSharedPrefs(this, spEntries[0], itemsCompleted)
            DbHelper.storeInSharedPrefs(this, spEntries[1], itemsHighPrio)
            DbHelper.storeInSharedPrefs(this, spEntries[2], itemsIterative)
        }

        output?.onSPStoreSucceed(mapOf("itemsCompleted" to itemsCompleted, "itemsHighPrio" to itemsHighPrio, "itemsIterative" to itemsIterative))
    }

    override fun getGoalInformationFromSharedPrefs(month: Int, year: Int) {
        val context = Aimission.getAppContext()
        val spEntry = getCurrentSPEntry(month, year)

        context?.apply {
            val itemsDone = DbHelper.getSharedPrefsValueAsInt(this, spEntry[0])
            val itemsHighPrio = DbHelper.getSharedPrefsValueAsInt(this, spEntry[1])
            val itemsIterative = DbHelper.getSharedPrefsValueAsInt(this, spEntry[2])
            output?.onItemInformationFromSharedPrefSucceed(itemsDone, itemsHighPrio, itemsIterative)
        }
    }

    override fun updateGoals() {
        for (item in goals) {
            updateGoalsInDb(item)
        }
    }

    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun getGoalsFromDb(
            userId: String,
            data: DataSnapshot,
            currentMonth: Int,
            currentYear: Int
    ): ArrayList<Goal> {
        userId.apply {
            return convertDataInGoals(data, currentMonth, currentYear)
        }
    }

    private fun updateGoalsInDb(updatedItem: Goal): Boolean {
        updatedItem.id.apply {
            val key = getGoalTableReference().child(getCurrentUserId())
            key.child(this).setValue(updatedItem)

            Log.i(TAG, "key is $key")
            return true
        }
    }
    
    private fun getIterativeItems(goals: ArrayList<Goal>) =
            goals.filter { goal ->
                goal.isComingBack
            }

    private fun getHighPriorityItems(goals: ArrayList<Goal>) =
        goals.filter { goal ->
            goal.isHighPriority
        }

    private fun getGoalsCompleted(goals: ArrayList<Goal>) =
        goals.filter { goal ->
            goal.status == Status.DONE
        }

    private fun getCurrentSPEntry(month: Int?, year: Int?): Array<String> {
        val result = Array<String>(3) { "" }

        month?.let { month ->
            year?.let { year ->
                result[0] = "amountItemsCompleted_$month$year"
                result[1] = "amountItemsHighPriority_$month$year"
                result[2] = "amountIterativeItems_$month$year"
            }
        }
        return result
    }
}

