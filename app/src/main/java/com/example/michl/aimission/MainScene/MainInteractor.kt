package com.example.michl.aimission.MainScene

import android.util.Log
import com.example.michl.aimission.Helper.convertMonthItem
import com.example.michl.aimission.Helper.getMonthAsText
import com.example.michl.aimission.Helper.getMonthItem
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

    // Get all month items from all user's items.
    private fun getMonthItems(items: ArrayList<AimItem?>): ArrayList<MonthItem> {
        var result = ArrayList<MonthItem>()
        var aimAmount = 1
        var aimsSucceeded = 0
        var year = 0
        var month: Month? = null

        for (item in items) {

            when (month) {
                null -> {
                    month = getMonthItem(item?.month)
                    year = item?.year ?: 0
                    if (item?.status == Status.DONE)
                        aimsSucceeded++
                }
                getMonthItem(item?.month) -> {
                    aimAmount++
                    if (item?.status == Status.DONE)
                        aimsSucceeded++

                    if (item == items.get(items.size - 1)) {
                        result.add(MonthItem(month?.name, aimAmount, getPercentOfSucceededAims(aimsSucceeded, aimAmount), month, year))
                    }
                }
                else -> {
                    result.add(MonthItem(month.name, aimAmount, getPercentOfSucceededAims(aimsSucceeded, aimAmount), month, year))

                    month = getMonthItem(item?.month)
                    aimAmount = 1

                    if (item?.status == Status.DONE)
                        aimsSucceeded = 1
                    else
                        aimsSucceeded = 0

                    if (item == items.get(items.size - 1)) {
                        result.add(MonthItem(month?.name, aimAmount, getPercentOfSucceededAims(aimsSucceeded, aimAmount), month, year))
                    }
                }
            }
        }

        return result
    }

    private fun createCurrentMonthItem(): MonthItem {
        // Calender functions month range is [0..11] not [1..12] so we have to convert it later
        val currentMonthDate = Calendar.getInstance().get(Calendar.MONTH)
        val currentMonth = convertMonthItem(currentMonthDate)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val monthAsText = getMonthAsText(currentMonth)
        return MonthItem(monthAsText, 0, 0, currentMonth, currentYear)
    }

    private fun getPercentOfSucceededAims(aimSucceeded: Int, aimAmount: Int) = aimSucceeded * 100 / aimAmount
}