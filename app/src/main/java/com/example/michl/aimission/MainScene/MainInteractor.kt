package com.example.michl.aimission.MainScene

import android.util.Log
import com.example.michl.aimission.Models.*
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface MainInteractorInput {
    fun getUsersMonthList(data:DataSnapshot)

}

class MainInteractor : MainInteractorInput {

    var output: MainPresenterInput? = null

    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    override fun getUsersMonthList(data:DataSnapshot) {
        val userId = getCurrentUserId()
        val months = ArrayList<MonthItem>()
        // todo get all active months for userId from firebase dynamically

        // get all months with at least one aim



        var query = FirebaseDatabase.getInstance().reference.child("Aim").child(userId)
        query.addValueEventListener(object: ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(data: DataSnapshot) {

                var items = ArrayList<AimItem?>()
                for (dataset in data.children)
                {
                    items.add(dataset.getValue(AimItem::class.java))

                    Log.i(TAG,"Found item ${dataset.getValue(AimItem::class.java)}")

                }

                // call function which calculates month list and item amount per month..

            }
        })

        val monthItem1 = MonthItem("Januar 2019",12,10, Month.JANUARY,2019)
        val monthItem2 = MonthItem("Februar 2019",10,4, Month.FEBRUARY, 2019)
        val monthItem3 = MonthItem("März 2019",2,0,Month.MARCH,2019)
        val monthItem4 = MonthItem("April 2019", 22,0,Month.APRIL,2019)
        val monthItem5 = MonthItem("Mai 2019",2,1,Month.MAY,2019)

        months.add(monthItem1)
        months.add(monthItem2)
        months.add(monthItem3)
        months.add(monthItem4)
        months.add(monthItem5)

        output?.onMonthItemsLoadedSuccessfully(months)
    }


    // get all months from all items from list. after this function we generate a list of month items for mainfragment
    // todo in progress
    private fun getMonthAndAmount(items:ArrayList<AimItem?>)
    {
        var monthList = ArrayList<MonthItem>()
        for (item in items)
        {
            getMonth(item?.month)

        }
    }

    // get month of current item
    // todo in progress
    private fun getMonth(month:Int?)
    {
//      month?.apply {
//          when (this)
//              is Month.JANUARY -> {

//      }
//      }
    }
}