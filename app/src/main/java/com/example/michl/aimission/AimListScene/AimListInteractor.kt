package com.example.michl.aimission.AimListScene

import android.util.Log
import com.example.michl.aimission.Helper.DateHelper.DateHelper.convertDataInAimItem
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

interface AimListInteractorInput {
    fun getItems(userId:String, data: DataSnapshot, month: Month, year: Int)
    fun changeItemProgress(item: AimItem?):Boolean
}

class AimListInteractor : AimListInteractorInput {

    var output: AimListPresenterInput? = null

    override fun getItems(userId:String, data: DataSnapshot, month: Month, year: Int) {
        val userId = getCurrentUserId()

        if (userId.isNullOrEmpty())
            output?.onNoUserIdExists()
        else {
            val items = createNewItemListFromDb(userId, data, month, year)
            output?.onItemsLoadedSuccessfully(items)
        }
    }

    override fun changeItemProgress(item: AimItem?):Boolean {
        //First we send an update to database and change the progress status either in "done" if it was "open" previously or into "subaim + 1"
        //if it has several sub aims in it. Then we go back to list adapter and update the ui.

        if (item == null)
            return false

        // change progress status
        if (item.status == Status.DONE)
            item.status = Status.OPEN
        else if (item.status == Status.OPEN)
            item.status = Status.DONE

        return updateAimItemInDb(item)
    }


    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun createNewItemListFromDb(userId: String, data: DataSnapshot, currentMonth: Month, currentYear: Int): ArrayList<AimItem> {
        userId?.apply {

            return convertDataInAimItem(data, currentMonth, currentYear)
        }
        Log.e(TAG, "Couldn't get current user id and so was unable to read aim items from user.")
        return ArrayList()
    }

    private fun updateAimItemInDb(updatedItem:AimItem):Boolean
    {
        if (updatedItem == null)
            return false
        updatedItem?.id?.apply {
            val reference = FirebaseDatabase.getInstance().getReference("Aim")

            var key = reference.child(getCurrentUserId())
            key.child(this).setValue(updatedItem)

            Log.i(TAG, "key is $key")
            return true
        }
        return false
    }
}

