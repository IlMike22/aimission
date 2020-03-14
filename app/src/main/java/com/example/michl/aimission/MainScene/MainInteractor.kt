package com.example.michl.aimission.MainScene

import android.util.Log
import com.example.michl.aimission.Models.*
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getCurrentUserId
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

interface MainInteractorInput {
    fun getUsersMonthList(data: DataSnapshot)
}

class MainInteractor : MainInteractorInput {
    var output: MainPresenterInput? = null
    private val goals = ArrayList<Goal?>()

    override fun getUsersMonthList(data: DataSnapshot) {
        // get all months with at least one aim
        val query = FirebaseDatabase.getInstance().reference.child("Aim").child(getCurrentUserId()).orderByChild("month")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "Firebase data sync was canceled.")
            }

            override fun onDataChange(data: DataSnapshot) {
                clearDeprecatedGoals(goals)

                for (dataset in data.children) {
                    try {
                        goals.add(dataset.getValue(Goal::class.java))
                    }
                    catch(exception:Exception) {
                        Log.e(TAG,"Error while converting goals from dataset. Details: ${exception.message}")
                        break
                    }
                }

                if (goals.size > 0) {
                    val monthItems = getMonthItems(goals)
                    val currentMonthItem = createNewMonth()

                    if (!monthItems.containsMonthItem(currentMonthItem.month, currentMonthItem.year)) {
                        monthItems.add(currentMonthItem)

                        //todo move it to aim list
//                        val defaultGoals = getDefaultGoals(goals)
//                        Log.i(TAG,"Found some default goals: $defaultGoals")
//                        if (defaultGoals.isNotEmpty())
//                            //todo add these items to new month. Therefore you have to return these items and create them

                    }

                    output?.onMonthsLoaded(goals, monthItems)

                } else {
                    val firstItem = createNewMonth()
                    output?.onEmptyMonthsLoaded(firstItem)
                }
            }
        })
    }

    // Get all month aims from all user's aims.
    private fun getMonthItems(aims: ArrayList<Goal?>): ArrayList<MonthItem> {
        val result = ArrayList<MonthItem>()
        var aimAmount = 0
        var aimSucceeded = 0
        var currentMonth = 0
        var currentYear = 0

        for (aim in aims) {

            if (currentMonth == 0) {
                currentMonth = aim?.month ?: -1
                currentYear = aim?.year ?: -1
                aimAmount++
                if (aim?.status == Status.DONE)
                    aimSucceeded++

            } else {
                if (currentMonth != aim?.month ?: -1) {
                    result.add(MonthItem(
                            name = getMonthName(currentMonth),
                            aimsAmount = aimAmount,
                            aimsSucceeded = aimSucceeded,
                            month = currentMonth,
                            year = currentYear,
                            isFirstStart = false))
                    aimAmount = 1
                    if (aim?.status == Status.DONE)
                        aimSucceeded = 1
                    else aimSucceeded = 0

                    currentMonth = aim?.month ?: -1
                    currentYear = aim?.year ?: -1
                } else {
                    aimAmount++
                    if (aim?.status == Status.DONE)
                        aimSucceeded++
                }

                if (aim == aims.get(aims.size - 1)) { // we reached last item of list
                    result.add(MonthItem(name = getMonthName(
                            month = aim?.month ?: -1),
                            aimsAmount = aimAmount,
                            aimsSucceeded = aimSucceeded,
                            month = aim?.month ?: -1,
                            year = aim?.year ?: -1,
                            isFirstStart = false
                    ))
                }
            }
        }
        return result
    }

    private fun createNewMonth(): MonthItem {
        // Calender functions month range is [0..11] not [1..12] so we have to convert it later
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return MonthItem(name = getMonthName(currentMonth),
                aimsSucceeded = 0,
                aimsAmount = 0,
                month = currentMonth,
                year = currentYear,
                isFirstStart = true)
    }

    private fun clearDeprecatedGoals(goalds: ArrayList<Goal?>): Unit {
        goals.clear()
    }

    //todo 01.02. this method needs to remain here because only here we have access to all months and items
    // in AimList we have only access to selected month.
    // maybe we store the value here in sp and get them back in aim list scene or you should think about how
    // you can transfer this information to aimlist scene in an other way..
    private fun getDefaultGoals(goals: List<Goal?>): ArrayList<Goal> {
        val defaultGoals = ArrayList<Goal>()
        goals.forEach { goal ->
            goal?.apply {
                if (goal.isComingBack) {
                    defaultGoals.add(goal)
                }
            }
        }
        return defaultGoals
    }
}