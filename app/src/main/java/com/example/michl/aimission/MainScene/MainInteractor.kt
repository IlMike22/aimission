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

    override fun getUsersMonthList(data: DataSnapshot) {

        // get all months with at least one aim

        var query = FirebaseDatabase.getInstance().reference.child("Aim").child(getCurrentUserId()).orderByChild("month")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "Firebase data sync was canceled.")
            }

            override fun onDataChange(data: DataSnapshot) {

                var items = ArrayList<AimItem?>()
                for (dataset in data.children) {
                    items.add(dataset.getValue(AimItem::class.java))

                    Log.i(TAG, "Found item ${dataset.getValue(AimItem::class.java)}")
                }

                if (items.size > 0) {
                    val userMonthList = getMonthItems(items)
                    Log.i(TAG, userMonthList.size.toString())

                    val currentMonthItem = createCurrentMonthItem()

                    if (!userMonthList.containsMonthItem(currentMonthItem.month, currentMonthItem.year)) {
                        userMonthList.add(currentMonthItem)
                    }

                    output?.onMonthItemsLoadedSuccessfully(items, userMonthList)

                } else {
                    //User does not have any aim defined yet. Create current month item for list so he can add his first aim.
                    //Get current month
                    val firstItem = createCurrentMonthItem()
                    output?.onEmptyMonthListLoaded(firstItem)
                }
            }
        })
    }

    // Get all month aims from all user's aims.
    private fun getMonthItems(aims: ArrayList<AimItem?>): ArrayList<MonthItem> {

        // we want to return all months with their specific properties like amount of aims, percentage (can be calculated),
        // aimsSucceed

        var result = ArrayList<MonthItem>()
        var aimAmount = 0
        var aimSucceeded = 0
        var currentMonth = 0
        var currentYear = 0

        //only for testing month function

        /*
        aims.add(AimItem("1", "b", "desc", 2, true, Status.OPEN, Genre.EDUCATION, 10, 2019, false))
        aims.add(AimItem("2", "a", "desc", 0, false, Status.OPEN, Genre.EDUCATION, 10, 2019, false))
        aims.add(AimItem("3", "v", "desc", 0, true, Status.OPEN, Genre.EDUCATION, 9, 2019, false))
        aims.add(AimItem("4", "e", "desc", 0, true, Status.DONE, Genre.EDUCATION, 8, 2019, false))
        aims.add(AimItem("5", "zh", "desc", 5, false, Status.DONE, Genre.EDUCATION, 8, 2019, false))
        aims.add(AimItem("6", "z", "desc", 0, false, Status.OPEN, Genre.EDUCATION, 8, 2019, false))
        aims.add(AimItem("7", "b", "desc", 2, true, Status.OPEN, Genre.EDUCATION, 8, 2019, false))
        aims.add(AimItem("8", "a", "desc", 0, false, Status.DONE, Genre.EDUCATION, 7, 2019, false))
        aims.add(AimItem("9", "v", "desc", 0, true, Status.OPEN, Genre.EDUCATION, 7, 2019, false))
        */


        for (aim in aims) {

            if (currentMonth == 0) {
                currentMonth = aim?.month ?: -1
                currentYear = aim?.year ?: -1
                aimAmount++
                if (aim?.status == Status.DONE)
                    aimSucceeded++

            } else {
                if (currentMonth != aim?.month ?: -1) {
                    // new month in item available
                    // store previous month and reset values
                    result.add(MonthItem(getMonthName(currentMonth), aimAmount, aimSucceeded, currentMonth, currentYear))
                    aimAmount = 1
                    if (aim?.status == Status.DONE)
                        aimSucceeded = 1
                    else aimSucceeded = 0

                    currentMonth = aim?.month ?: -1
                    currentYear = aim?.year ?: -1
                } else {
                    // just another item for current month
                    aimAmount++
                    if (aim?.status == Status.DONE)
                        aimSucceeded++
                }


                if (aim == aims.get(aims.size - 1)) { // we reached last item of list
                    result.add(MonthItem(getMonthName(aim?.month
                            ?: -1), aimAmount, aimSucceeded, aim?.month
                            ?: -1, aim?.year ?: -1))
                }
            }
        }
        return result
    }

    private fun createCurrentMonthItem(): MonthItem {
        // Calender functions month range is [0..11] not [1..12] so we have to convert it later
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return MonthItem(getMonthName(currentMonth), 0, 0, currentMonth, currentYear)
    }
}