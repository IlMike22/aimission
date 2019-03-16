package com.example.michl.aimission.AimListScene

import android.util.Log
import com.example.michl.aimission.Helper.DateHelper.DateHelper.convertDataInAimItem
import com.example.michl.aimission.Helper.DateHelper.DateHelper.getCurrentMonth
import com.example.michl.aimission.Helper.DateHelper.DateHelper.getCurrentYear
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

interface AimListInteractorInput {
    fun getItems(data: DataSnapshot, month:Int,year:Int)
}

class AimListInteractor : AimListInteractorInput {
    var output: AimListPresenterInput? = null

    override fun getItems(data: DataSnapshot, month:Int, year:Int) {
        val userId = getCurrentUserId()

        if (userId.isNullOrEmpty())
            output?.onNoUserIdExists()
        else {
            val items = createNewItemListFromDb(userId, data, month, year)
            output?.onItemsLoadedSuccessfully(items)
        }
    }


    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun createNewItemListFromDb(userId: String, data: DataSnapshot, currentMonth:Int, currentYear:Int): ArrayList<AimItem> {
        userId?.apply {

            return convertDataInAimItem(data, currentMonth, currentYear)


        }
        Log.e(TAG,"Couldn't get current user id and so was unable to read aim items from user.")
        return ArrayList()
    }
}

