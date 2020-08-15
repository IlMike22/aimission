package com.example.michl.aimission.LandingpageScene.implementation

import android.util.Log
import com.example.michl.aimission.Utility.DateHelper
import com.example.michl.aimission.LandingpageScene.ILandingpageInteractor
import com.example.michl.aimission.LandingpageScene.ILandingpagePresenter
import com.example.michl.aimission.Models.*
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getCurrentUserId
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class LandingpageInteractor : ILandingpageInteractor {
    var output: ILandingpagePresenter? = null
    private val goals = ArrayList<Goal?>()

    override fun getUsersMonthList(data: DataSnapshot) {
        val query = FirebaseDatabase.getInstance().reference.child("Aim").child(getCurrentUserId()).orderByChild("month")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "Firebase data sync was canceled.")
            }

            override fun onDataChange(data: DataSnapshot) {
                var isNewMonthAdded = false
                clearDeprecatedGoals(goals)
                val currentMonth = createNewMonth()

                for (dataset in data.children) {
                    try {
                        goals.add(dataset.getValue(Goal::class.java))
                    } catch (exception: Exception) {
                        Log.e(TAG, "Error while converting goals from dataset. Details: ${exception.message}")
                    }
                }

                if (goals.size > 0) {
                    val months = getMonths(goals)
                    if (!months.containsMonth(currentMonth.month, currentMonth.year)) {
                        months.add(currentMonth)
                        goals.addAll(setIterativeGoals())

                    }
                    output?.onMonthsLoaded(
                            goals = goals,
                            months = months
                    )

                    return
                }

                if (getIterativeGoals().isEmpty()) {
                    output?.onEmptyMonthsLoaded(currentMonth)
                    return
                }

                goals.addAll(setIterativeGoals())

                output?.onMonthsLoaded(
                        goals = goals,
                        months = getMonths(goals))
            }
        })
    }

    private fun getMonths(goals: ArrayList<Goal?>): ArrayList<Month> {
        val result = ArrayList<Month>()
        var goalAmount = 0
        var goalsCompleted = 0
        var currentMonth = 0
        var currentYear = 0

        for (goal in goals) {
            if (currentMonth == 0) {
                currentMonth = goal?.month ?: -1
                currentYear = goal?.year ?: -1
                goalAmount++
                if (goal?.status == Status.DONE)
                    goalsCompleted++

            } else {
                if (currentMonth != goal?.month ?: -1) {
                    result.add(Month(
                            name = getMonthName(currentMonth),
                            goalAmount = goalAmount,
                            goalsCompleted = goalsCompleted,
                            month = currentMonth,
                            year = currentYear,
                            isFirstStart = false,
                            isDepecrecated = true))
                    goalAmount = 1
                    if (goal?.status == Status.DONE)
                        goalsCompleted = 1
                    else goalsCompleted = 0

                    currentMonth = goal?.month ?: -1
                    currentYear = goal?.year ?: -1
                } else {
                    goalAmount++
                    if (goal?.status == Status.DONE)
                        goalsCompleted++
                }

                if (goal == goals.get(goals.size - 1)) {
                    result.add(Month(name = getMonthName(
                            month = goal?.month ?: -1),
                            goalAmount = goalAmount,
                            goalsCompleted = goalsCompleted,
                            month = goal?.month ?: -1,
                            year = goal?.year ?: -1,
                            isFirstStart = false,
                            isDepecrecated = isMonthDeprecated(
                                    month = goal?.month ?: -1,
                                    year = goal?.year ?: -1
                            )
                    ))
                }
            }
        }
        return result
    }

    private fun createNewMonth(): Month {
        // Calender functions month range is [0..11] not [1..12] so we have to convert it later
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return Month(name = getMonthName(currentMonth),
                goalsCompleted = 0,
                goalAmount = 0,
                month = currentMonth,
                year = currentYear,
                isFirstStart = true,
                isDepecrecated = false)
    }

    private fun clearDeprecatedGoals(goalds: ArrayList<Goal?>): Unit {
        goals.clear()
    }

    private fun isMonthDeprecated(month: Int, year: Int): Boolean {
        if (month == DateHelper.getCurrentMonth() && year == DateHelper.getCurrentYear()) {
            return false
        }
        return true
    }

    private fun setIterativeGoals(): ArrayList<Goal> {
        val iterativeGoals = getIterativeGoals()
        iterativeGoals.forEach { iterativeGoal ->
            DbHelper.createOrUpdateGoal(
                    userId = getCurrentUserId(),
                    goal = iterativeGoal
            )
        }

        return iterativeGoals
    }

    private fun getIterativeGoals(): ArrayList<Goal> =
            DbHelper.getIterativeGoals(goals)

}