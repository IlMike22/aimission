package com.example.michl.aimission.AimListScene

import android.content.Context
import android.util.Log
import com.example.michl.aimission.Helper.DateHelper.DateHelper.convertDataInAimItem
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getAimTableReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

interface AimListInteractorInput {
    fun getItems(context: Context?, userId: String, data: DataSnapshot, month: Month, year: Int)
    fun changeItemProgress(item: AimItem?)
    fun getItemInformationFromSharedPrefs(context: Context, month: Month, year: Int)
}

class AimListInteractor : AimListInteractorInput {

    var output: AimListPresenterInput? = null

    //todo context should not be available in interactor, find a way to avoid context parameter here
    override fun getItems(context: Context?, userId: String, data: DataSnapshot, month: Month, year: Int) {
        val userId = getCurrentUserId()

        val spKeyItemsCompleted = "amountItemsCompleted_$month$year"
        val spKeyItemsHighPrio = "amountItemsHighPriority_$month$year"
        val spKeyItemsIterative = "amountIterativeItems_$month$year"

        if (userId.isNullOrEmpty())
            output?.onNoUserIdExists()
        else {
            val items = createNewItemListFromDb(userId, data, month, year)

            // get information about the items and store this information in shared prefs.
            context?.apply {
                DbHelper.storeInSharedPrefs(this, spKeyItemsCompleted, getAllCompletedItems(items).size)
                DbHelper.storeInSharedPrefs(this, spKeyItemsHighPrio, getHighPriorityItems(items).size)
                DbHelper.storeInSharedPrefs(this, spKeyItemsIterative, getIterativeItems(items).size)
            }

            output?.onItemsLoadedSuccessfully(items, month, year)
        }
    }

    override fun changeItemProgress(item: AimItem?) {
        //First we send an update to database and change the progress status either in "done" if it was "open" previously or into "subaim + 1"
        //if it has several sub aims in it. Then we go back to list adapter and update the ui.

        item?.apply {
            // change progress status
            if (item?.status == Status.DONE)
                item.status = Status.OPEN
            else if (item?.status == Status.OPEN)
                item?.status = Status.DONE

            if (updateAimItemInDb(item))
                output?.onItemStatusChanged(item)
            else
                output?.onItemStatusChangeFailed(item)
        } ?: run {
            output?.onItemStatusChangeFailed(null)
        }
    }

    //todo context should not be available in interactor, find a way to avoid context parameter here
    override fun getItemInformationFromSharedPrefs(context: Context, month: Month, year: Int) {

        //todo redundant code (see storeSP)
        val spKeyItemsCompleted = "amountItemsCompleted_$month$year"
        val spKeyItemsHighPrio = "amountItemsHighPriority_$month$year"
        val spKeyItemsIterative = "amountIterativeItems_$month$year"


        //todo fix this problem. all returned values from shared prefs are -1
        context?.apply {
            val itemsDoneAmount = DbHelper.getSharedPrefsValueAsInt(this, spKeyItemsCompleted)
            val itemsHighPrioAmount = DbHelper.getSharedPrefsValueAsInt(this, spKeyItemsHighPrio)
            val itemsIterativeAmount = DbHelper.getSharedPrefsValueAsInt(this, spKeyItemsIterative)
            output?.onItemInformationFromSharedPrefSucceed(itemsDoneAmount, itemsHighPrioAmount, itemsIterativeAmount)
        }


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

    private fun updateAimItemInDb(updatedItem: AimItem): Boolean {
        updatedItem?.id?.apply {

            var key = getAimTableReference().child(getCurrentUserId())
            key.child(this).setValue(updatedItem)

            Log.i(TAG, "key is $key")
            return true
        }
        return false
    }

    /*
      Returns all items that have flag comesBack set in an ArrayList.
      //todo these three functions have a lot of redundant code. This can be optimized.
   */
    private fun getIterativeItems(items: ArrayList<AimItem>): ArrayList<AimItem> {
        var result = ArrayList<AimItem>()
        for (item in items) {
            if (item.comesBack == true)
                result.add(item)
        }

        return result
    }

    /*
        Returns all high priority items in an ArrayList.
        //todo these three functions have a lot of redundant code. This can be optimized.
     */
    private fun getHighPriorityItems(items: ArrayList<AimItem>): ArrayList<AimItem> {
        var result = ArrayList<AimItem>()
        for (item in items) {
            if (item.highPriority == true)
                result.add(item)
        }

        return result
    }

    /*
        Returns all completed items in an ArrayList.
        //todo these three functions have a lot of redundant code. This can be optimized.
     */
    private fun getAllCompletedItems(items: ArrayList<AimItem>): ArrayList<AimItem> {
        var result = ArrayList<AimItem>()
        for (item in items) {
            if (item.status == Status.DONE)
                result.add(item)
        }

        return result
    }
}

