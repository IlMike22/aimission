package com.example.michl.aimission.AimListScene

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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

interface AimListInteractorInput {
    fun getItems(userId: String, data: DataSnapshot, month: Month, year: Int)
    fun changeItemProgress(item: AimItem?)
    fun getAllIterativeItems(userId:String,data:DataSnapshot, month:Month?=null, year:Int?=null)
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
    override fun getAllIterativeItems(userId: String, data: DataSnapshot, month: Month?, year: Int?) {

        val query = DbHelper.getAimTableReference().child(userId).child("comesBack").equalTo(true)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "A data changed error occured.")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //get all items that has comesBack set to true
            }
        })
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

    private fun getAllIterativeItems(items:ArrayList<AimItem>):ArrayList<AimItem>
    {
        var itemList = ArrayList<AimItem>()
        for (item in items)
        {
            item.comesBack?.apply {
                if (this)
                {
                    itemList.add(item)
                }
            }
        }

        return itemList
    }
}

