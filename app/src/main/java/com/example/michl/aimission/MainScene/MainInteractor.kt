package com.example.michl.aimission.MainScene

import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.Models.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

interface MainInteractorInput {
    fun getUsersMonthList()

}

class MainInteractor : MainInteractorInput {

    var output: MainPresenterInput? = null

    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    override fun getUsersMonthList() {
        val userId = getCurrentUserId()
        val months = ArrayList<MonthItem>()
        // todo get all active months for userId from firebase dynamically

        val monthItem1 = MonthItem("Januar 2019",12,10, 1,2019)
        val monthItem2 = MonthItem("Februar 2019",10,4, 2, 2019)
        val monthItem3 = MonthItem("März 2019",2,0,3,2019)
        val monthItem4 = MonthItem("April 2019", 22,0,4,2019)

        months.add(monthItem1)
        months.add(monthItem2)
        months.add(monthItem3)
        months.add(monthItem4)

        output?.onMonthItemsLoadedSuccessfully(months)
    }
}