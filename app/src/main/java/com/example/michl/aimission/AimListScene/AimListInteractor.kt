package com.example.michl.aimission.AimListScene

import com.example.michl.aimission.Helper.DateHelper.DateHelper.getCurrentMonth
import com.example.michl.aimission.Helper.DateHelper.DateHelper.getCurrentYear
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
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
        val itemList = ArrayList<AimItem>()
        userId?.apply {

            val items = data.child(this).children
            for (item in items) {

                val month = (item.child("month").value as Long).toInt()
                val year = (item.child("year").value as Long).toInt()

                if (currentMonth == month && currentYear == year)
                {
                    // parsing all the aim data..

                    val id = item.child("id").value as String
                    val title = item.child("title").value as String
                    val statusString = item.child("status").value as String
                    val repeatCount = item.child("repeatCount").value as Int
                    val genreString = item.child("genre").value as String
                    val isHighPriority = item.child("highPriority").value as Boolean
                    var description = item.child("description").value as String

                    // convert status and genre in enums

                    val status = Status.valueOf(statusString)
                    val genre = Genre.valueOf(genreString)

                    val newAimItem = AimItem(id, title, description, repeatCount, isHighPriority, status, genre, month, year)
                    itemList.add(newAimItem)
                }

            }
        }
        return itemList
    }
}

