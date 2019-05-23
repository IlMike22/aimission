package com.example.michl.aimission.MainScene

import android.util.Log
import com.example.michl.aimission.Helper.getCurrentUserId
import com.example.michl.aimission.Helper.getMonthAsText
import com.example.michl.aimission.Helper.getMonthItem
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
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
        val userId = getCurrentUserId()
        val months = ArrayList<MonthItem>()
        // todo get all active months for userId from firebase dynamically

        // get all months with at least one aim

        var query = FirebaseDatabase.getInstance().reference.child("Aim").child(userId).orderByChild("month")

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
                    Log.i(TAG,userMonthList.size.toString())

                    output?.onMonthItemsLoadedSuccessfully(items,userMonthList)
                }
                else
                {
                    //User does not have any aim defined yet. Create current month item for list so he can add his first aim.
                    //Get current month
                    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    val month = getMonthItem(currentMonth)
                    val monthAsText = getMonthAsText(month)

                    val firstItem = MonthItem(monthAsText,0,0,month,currentYear)
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

        for (item in items) {
            val currentMonth = getMonthItem(item?.month)

            if (monthList.contains(currentMonth))
            {
                aimAmountPerMonth++ // month already in list, increment counter
            }
            else
            {
                aimAmountPerMonth++
                //todo we still have to add solution percent per month
                result.add(MonthItem(getMonthAsText(currentMonth),aimAmountPerMonth,0,currentMonth,item?.year?:0))
                monthList.add(getMonthItem(item?.month))
                aimAmountPerMonth = 0 //reset counter, new month incoming
            }
        }

        return result
    }
}