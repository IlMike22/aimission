package com.example.michl.aimission.goalsScene.implementation

import android.util.Log
import com.example.michl.aimission.goalsScene.IGoalsInteractor
import com.example.michl.aimission.goalsScene.IGoalsPresenter
import com.example.michl.aimission.utitlity.DateHelper
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.models.Month
import com.example.michl.aimission.models.Status
import com.example.michl.aimission.utitlity.Aimission
import com.example.michl.aimission.utitlity.Aimission.Companion.roomDb
import com.example.michl.aimission.utitlity.DateHelper.DateHelper.convertDataInGoals
import com.example.michl.aimission.utitlity.DateHelper.DateHelper.getFireBaseUser
import com.example.michl.aimission.utitlity.DbHelper
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import com.example.michl.aimission.utitlity.DbHelper.Companion.getGoalTableReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import org.jetbrains.anko.doAsync

class GoalsInteractor : IGoalsInteractor {
    var output: IGoalsPresenter? = null
    var goals = ArrayList<Goal>()
    var defaultGoalsAmount = 0

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
        goals = getGoalsFromDb(userId, data, monthItem.month, monthItem.year)

        if (monthItem.isFirstStart) {
            // get all default goals and create them for new month, set isFirstStart then to false and save this in firebase
            doAsync {
                defaultGoalsAmount = roomDb?.DefaultGoalsDao()?.getDefaultGoalAmount() ?: 0
                val defaultGoals = roomDb?.DefaultGoalsDao()?.getDefaultGoalsFromRoomDb()

                defaultGoals?.forEach { defaultGoal ->
                    goals.add(
                            modifyDefaultGoalForCurrentMonth(
                                    defaultGoal = defaultGoal,
                                    monthItem = monthItem
                            )
                    )
                }

                onGoalsLoaded(goals, monthItem.month, monthItem.year)
                return@doAsync
            }
        }

        onGoalsLoaded(goals, monthItem.month, monthItem.year)
    }

    override fun changeGoalProgress(goal: Goal?, position: Int) {
        goal?.apply {
            setGoalStatus(this, repeatCount)

            val isGoalUpdateFailed = !DbHelper.createOrUpdateGoal(getFireBaseUser(), goal)

            if (isGoalUpdateFailed) {
                Log.e(TAG, "GoalsInteractor: Goal could not be updated after progress changed.")
            }

            // update item list
            goals.get(position).status = status
            output?.onGoalStatusChanged(position)

        } ?: run {
            output?.onGoalStatusChangedError(null, position)
        }
    }

    override fun storeGoalsInSharedPreferences(goals: ArrayList<Goal>) {
        val context = Aimission.getAppContext()
        val month = goals[0].month
        val year = goals[0].year
        val spEntries = getCurrentSPEntry(month, year)
        val itemsCompleted = getGoalsCompleted(goals).size
        val itemsHighPrio = getHighPriorityItems(goals).size
        val itemsIterative = getIterativeItems(goals).size


        // stores current state of item information for this month in sp and returns the result in a dict
        if (goals.size == 0) {
            output?.onSharedPreferencesStoredError("No items found.")
            return
        }

        // get information about the items and store this information in shared prefs.
        context?.apply {
            DbHelper.storeInSharedPrefs(this, spEntries[0], itemsCompleted)
            DbHelper.storeInSharedPrefs(this, spEntries[1], itemsHighPrio)
            DbHelper.storeInSharedPrefs(this, spEntries[2], itemsIterative)
        }

        output?.onSPStoreSuccess(mapOf("itemsCompleted" to itemsCompleted, "itemsHighPrio" to itemsHighPrio, "itemsIterative" to itemsIterative))
    }

    override fun getGoalInformationFromSharedPrefs(month: Int, year: Int) {
        val context = Aimission.getAppContext()
        val spEntry = getCurrentSPEntry(month, year)

        context?.apply {
            val itemsDone = DbHelper.getSharedPrefsValueAsInt(this, spEntry[0])
            val itemsHighPrio = DbHelper.getSharedPrefsValueAsInt(this, spEntry[1])
            val itemsIterative = DbHelper.getSharedPrefsValueAsInt(this, spEntry[2])
            output?.onItemInformationFromSharedPrefSuccess(itemsDone, itemsHighPrio, itemsIterative)
        }
    }

    override fun updateGoals() {
        for (goal in goals) {
            updateGoalsInDb(goal)
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

    private fun onGoalsLoaded(
            goals: ArrayList<Goal>,
            month: Int,
            year: Int
    ) {
        output?.onGoalsLoaded(
                goals = goals,
                month = month,
                year = year,
                addedDefaultGoalsSize = defaultGoalsAmount
        )
    }

    private fun modifyDefaultGoalForCurrentMonth(
            defaultGoal: Goal,
            monthItem: Month
    ): Goal {
        defaultGoal.creationDate = DateHelper.currentDate.toString()
        defaultGoal.month = monthItem.month
        defaultGoal.year = monthItem.year
        defaultGoal.status = Status.OPEN
        return defaultGoal
    }

    private fun setGoalStatus(goal: Goal, repeatCount: Int) {
        goal.apply {
            if (repeatCount == 0) {
                if (goal.status == Status.DONE) {
                    goal.status = Status.OPEN
                    return
                }

                goal.status = Status.DONE
                return
            }

            goal.partGoalsAchieved = goal.partGoalsAchieved + 1

            if (goal.status == Status.DONE) {
                goal.status = Status.OPEN
                goal.partGoalsAchieved = 0
                return
            }

            if (goal.partGoalsAchieved == repeatCount) {
                goal.status = Status.DONE
                return
            }

            goal.status = Status.PROGRESS
        }
    }
}

