package com.example.michl.aimission.MainScene

import android.util.Log
import com.example.michl.aimission.Models.*
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getCurrentUserId
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

interface MainInteractorInput {
    fun getUsersMonthList(data: DataSnapshot)
}

class MainInteractor : MainInteractorInput {
    var output: MainPresenterInput? = null
    private val goals = ArrayList<AimItem?>()

    override fun getUsersMonthList(data: DataSnapshot) {
        // get all months with at least one aim
        val query = FirebaseDatabase.getInstance().reference.child("Aim").child(getCurrentUserId()).orderByChild("month")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "Firebase data sync was canceled.")
            }

            override fun onDataChange(data: DataSnapshot) {
                for (dataset in data.children) {
                    goals.add(dataset.getValue(AimItem::class.java))
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

                    output?.onMonthItemsLoadedSuccessfully(goals, monthItems)

                } else {
                    //User does not have any aim defined yet. Create current month item for list so he can add his first aim.
                    //Get current month
                    val firstItem = createNewMonth()
                    output?.onEmptyMonthListLoaded(firstItem)
                }
            }
        })
    }

    // Get all month aims from all user's aims.
    private fun getMonthItems(aims: ArrayList<AimItem?>): ArrayList<MonthItem> {
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

    //todo 01.02. this method needs to remain here because only here we have access to all months and items
    // in AimList we have only access to selected month.
    // maybe we store the value here in sp and get them back in aim list scene or you should think about how
    // you can transfer this information to aimlist scene in an other way..
    private fun getDefaultGoals(goals: List<AimItem?>): ArrayList<AimItem> {
        val defaultGoals = ArrayList<AimItem>()
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