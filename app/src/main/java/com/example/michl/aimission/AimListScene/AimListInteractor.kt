package com.example.michl.aimission.AimListScene

import android.util.Log
import com.example.michl.aimission.Helper.DateHelper.DateHelper.convertDataInAimItem
import com.example.michl.aimission.Helper.getIntFromMonth
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getAimTableReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

interface AimListInteractorInput {
    fun getItems(userId: String, data: DataSnapshot, month: Month, year: Int)
    fun changeItemProgress(item: AimItem?)
    fun getIterativeItems(month: Month? = null, year: Int? = null)
    fun getCompletedItems(userId: String, data: DataSnapshot, month: Month? = null, year: Int? = null)
    fun getHighPriorityItems(month:Month?=null, year:Int?=null)
}

class AimListInteractor : AimListInteractorInput {



    var output: AimListPresenterInput? = null

    override fun getItems(userId: String, data: DataSnapshot, month: Month, year: Int) {
        val userId = getCurrentUserId()

        if (userId.isNullOrEmpty())
            output?.onNoUserIdExists()
        else {
            val items = createNewItemListFromDb(userId, data, month, year)
            output?.onItemsLoadedSuccessfully(items)
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

    /*
        Returns all items that have flag comesBack set.
        Attention: At the moment it seems that there is no field comesBack in database so the result here will always be 0.
        First you have to setup the database so in the future an item which was saved has this field stored into the db.
     */
    override fun getIterativeItems(month: Month?, year: Int?) {

        try {
            val query = DbHelper.getAimTableReference().child(getCurrentUserId()).child("repeatCount").equalTo(2.0)
            query.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    val msg = "A data changed error occured. Details: ${databaseError.message}"
                    Log.i(TAG, msg)
                    output?.onIterativeItemsGotFailed(msg)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val items = convertDataInAimItem(dataSnapshot)
                    output?.onIterativeItemsGot(items)
                }
            })
        } catch (exc: IllegalArgumentException) {
            val msg = "Unable to query database. IllegalArgumentException was thrown. ${exc.message}"
            Log.e(TAG, msg)
            output?.onIterativeItemsGotFailed(msg)
        } catch (exc: Exception) {
            val msg = "Unable to query database. Unknown exception was thrown. ${exc.message}"
            Log.e(TAG, msg)
            output?.onIterativeItemsGotFailed(msg)
        }

    }

    override fun getCompletedItems(userId: String, data: DataSnapshot, month: Month?, year: Int?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun getHighPriorityItems(month: Month?, year: Int?) {
        try {
            var query:Query? = null
            query = if (month != null && year != null) {
                //todo get right syntax for query for highPriority true for month and year parameter set
                DbHelper.getAimTableReference().child(getCurrentUserId()).orderByChild("highPriority").equalTo(true).orderByChild("month").equalTo(getIntFromMonth(month).toDouble())
            } else
                DbHelper.getAimTableReference().child(getCurrentUserId()).child("highPriority").equalTo(true)

            query.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    val msg = "A data changed error occured. Details: ${databaseError.message}"
                    Log.i(TAG, msg)
                    output?.onIterativeItemsGotFailed(msg)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val items = convertDataInAimItem(dataSnapshot)
                    output?.onHighPriorityItemsGot(items)
                }
            })
        } catch (exc: IllegalArgumentException) {
            val msg = "Unable to query database. IllegalArgumentException was thrown. ${exc.message}"
            Log.e(TAG, msg)
            output?.onHighPriorityItemsGotFailed(msg)
        } catch (exc: Exception) {
            val msg = "Unable to query database. Unknown exception was thrown. ${exc.message}"
            Log.e(TAG, msg)
            output?.onHighPriorityItemsGotFailed(msg)
        }
    }
}

