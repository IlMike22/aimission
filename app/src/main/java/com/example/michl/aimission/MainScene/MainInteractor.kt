package com.example.michl.aimission.MainScene

import android.util.Log
import com.example.michl.aimission.Helper.getMonthAsText
import com.example.michl.aimission.Helper.getMonthItem
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.Models.containsMonthItem
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
        var monthList = ArrayList<Month>()
        var aimAmountPerMonth = 0
        var previousMonth: Month? = null

        //TODO bad naming. what is currentMonth, what is lastMonth? also there are some bugs finding months and naming them right
        for (item in items) {
            val itemMonth = getMonthItem(item?.month)
            if (previousMonth == null) //todo bug! we get the wrong month where items were stored because lastMonth is set to current month
                previousMonth = itemMonth

            if (monthList.contains(itemMonth)) {
                aimAmountPerMonth++ // month already in list, increment counter

            } else if (aimAmountPerMonth != 0) {
                // store previous month information in result and reset.
                result.add(MonthItem(getMonthAsText(previousMonth), aimAmountPerMonth, 0, previousMonth, item?.year
                        ?: 0))
                aimAmountPerMonth = 0 //reset counter, new month incoming
                previousMonth = null // reset last month
                //todo we still have to add solution percent per month

            } else {
                aimAmountPerMonth++
                monthList.add(getMonthItem(item?.month))
            }

            if (items.indexOf(item) == items.size - 1) {
                // we have reached to last item of array, store current item month and finish.
                result.add(MonthItem(getMonthAsText(itemMonth), aimAmountPerMonth, 0, itemMonth, item?.year
                        ?: 0))
            }
        }

        return result
    }

    private fun createCurrentMonthItem(): MonthItem {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val month = getMonthItem(currentMonth)
        val monthAsText = getMonthAsText(month)
        return MonthItem(monthAsText, 0, 0, month, currentYear)
    }
}