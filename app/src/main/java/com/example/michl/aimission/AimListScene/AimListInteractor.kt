package com.example.michl.aimission.AimListScene

import android.icu.util.Calendar
import com.example.michl.aimission.MainScene.MainPresenterInput
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Models.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.LocalDateTime

interface AimListInteractorInput
{
    fun getItems(data: DataSnapshot)
}

class AimListInteractor : AimListInteractorInput
{
    var output: AimListPresenterInput? = null

    override fun getItems(data: DataSnapshot) {
        val userId = getCurrentUserId()

        val firebaseDB = FirebaseDatabase.getInstance().reference

        if (userId.isNullOrEmpty())
            output?.onNoUserIdExists()
        else {
            val items = createNewItemListFromDb(userId, data)
            output?.onItemsLoadedSuccessfully(items)
        }
    }


    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun createNewItemListFromDb(userId: String, data: DataSnapshot): ArrayList<AimItem> {
        val itemList = ArrayList<AimItem>()
        userId?.apply {

            val items = data.child(this).children
            for (item in items) {
                // parsing all the aim data..
                val id = item.child("id").value as String
                val title = item.child("title").value as String
                val statusString = item.child("status").value as String
                val repeatCount = item.child("repeatCount").value as Long
                val genreString = item.child("genre").value as String
                val isHighPriority = item.child("highPriority").value as Boolean
                var description = item.child("description").value as String

                // convert status and genre in enums

                val status = Status.valueOf(statusString)
                val genre = Genre.valueOf(genreString)

                val newAimItem = AimItem(id, title, description, repeatCount, isHighPriority, status, genre, getCurrentMonth(), getCurrentYear())
                itemList.add(newAimItem)
            }
        }
        return itemList
    }

    private fun getCurrentMonth(): Int
    {
            var current = LocalDate.now()
            return current.month.value
    }

    private fun getCurrentYear():Int
    {
        return 2018
    }


}

