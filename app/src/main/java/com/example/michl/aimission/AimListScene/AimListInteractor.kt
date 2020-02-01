package com.example.michl.aimission.AimListScene

import android.util.Log
import com.example.michl.aimission.Helper.DateHelper.DateHelper.convertDataInAimItem
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getAimTableReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

interface AimListInteractorInput {
    fun getItems(userId: String, data: DataSnapshot)
    fun changeItemProgress(item: AimItem?, position: Int)
    fun storeItemInformationInSharedPref(items: ArrayList<AimItem>)
    fun getItemInformationFromSharedPrefs(month: Int, year: Int)
    fun updateItemList()
}

class AimListInteractor(
        val monthItem: MonthItem
) : AimListInteractorInput {

    var output: AimListPresenterInput? = null
    var items = ArrayList<AimItem>()

    //todo context should not be available in interactor, find a way to avoid context parameter here
    override fun getItems(userId: String, data: DataSnapshot) {
        val userId = getCurrentUserId()

        if (userId.isNullOrEmpty()) {
            output?.onNoUserIdExists()
            return
        }
        if (monthItem.isFirstStart) {
            // get all default goals and create them for new month, set isFirstStart then to false and save this in firebase
        }
        items = createNewItemListFromDb(userId, data, monthItem.month, monthItem.year)

        output?.onItemsLoadedSuccessfully(items, monthItem.month, monthItem.year)

    }

    override fun changeItemProgress(item: AimItem?, position: Int) {
        item?.apply {
            // change progress status
            if (item?.status == Status.DONE)
                item.status = Status.OPEN
            else if (item?.status == Status.OPEN)
                item?.status = Status.DONE

            // update item list
            items.get(position).status = item.status
            output?.onItemStatusChanged(item, position)

        } ?: run {
            output?.onItemStatusChangeFailed(null, position)
        }
    }


    override fun storeItemInformationInSharedPref(items: ArrayList<AimItem>) {
        // stores current state of item information for this month in sp and returns the result in a dict
        if (items.size == 0) {
            output?.onSPStoreFailed("No items found.")
            return
        }

        val context = Aimission.getAppContext()
        val month = items[0].month
        val year = items[0].year
        val spEntries = getCurrentSPEntry(month, year)

        val itemsCompleted = getAllCompletedItems(items).size
        val itemsHighPrio = getHighPriorityItems(items).size
        val itemsIterative = getIterativeItems(items).size

        // get information about the items and store this information in shared prefs.
        context?.apply {
            DbHelper.storeInSharedPrefs(this, spEntries[0], itemsCompleted)
            DbHelper.storeInSharedPrefs(this, spEntries[1], itemsHighPrio)
            DbHelper.storeInSharedPrefs(this, spEntries[2], itemsIterative)
        }

        output?.onSPStoreSucceed(mapOf("itemsCompleted" to itemsCompleted, "itemsHighPrio" to itemsHighPrio, "itemsIterative" to itemsIterative))

    }

    override fun getItemInformationFromSharedPrefs(month: Int, year: Int) {
        val context = Aimission.getAppContext()
        val spEntry = getCurrentSPEntry(month, year)

        context?.apply {
            val itemsDone = DbHelper.getSharedPrefsValueAsInt(this, spEntry[0])
            val itemsHighPrio = DbHelper.getSharedPrefsValueAsInt(this, spEntry[1])
            val itemsIterative = DbHelper.getSharedPrefsValueAsInt(this, spEntry[2])
            output?.onItemInformationFromSharedPrefSucceed(itemsDone, itemsHighPrio, itemsIterative)
        }
    }

    override fun updateItemList() {
        for (item in items) {
            updateAimItemInDb(item)
        }
    }

    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun createNewItemListFromDb(userId: String, data: DataSnapshot, currentMonth: Int, currentYear: Int): ArrayList<AimItem> {
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
            if (item.isComingBack == true)
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
            if (item.isHighPriority == true)
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

    private fun getCurrentSPEntry(month: Int?, year: Int?): Array<String> {
        var result = Array<String>(3) { "" }

        month?.let { month ->
            year?.let { year ->
                result[0] = "amountItemsCompleted_$month$year"
                result[1] = "amountItemsHighPriority_$month$year"
                result[2] = "amountIterativeItems_$month$year"
            }
        }

        return result
    }
}

