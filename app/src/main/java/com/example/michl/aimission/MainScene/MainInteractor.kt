package com.example.michl.aimission.MainScene

import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.Models.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

interface MainInteractorInput {
    fun updateItemList(data: DataSnapshot)
    fun getUsersMonthList()

}

class MainInteractor : MainInteractorInput {


    var output: MainPresenterInput? = null

    override fun updateItemList(data: DataSnapshot) {
        val userId = getCurrentUserId()
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


                val newAimItem = AimItem(id, title, description, repeatCount, isHighPriority, status, genre)
                itemList.add(newAimItem)
            }
        }
        return itemList
    }

    override fun getUsersMonthList() {
        val userId = getCurrentUserId()
        val months = ArrayList<MonthItem>()
        // todo get all active months for userId from firebase
        // first create two dummy items that are shown in mainfragment

        val monthItem1 = MonthItem("Januar 2019",12,10)
        val monthItem2 = MonthItem("Februar 2019",10,4)

        months.add(monthItem1)
        months.add(monthItem2)

        output?.onMonthItemsLoadedSuccessfully(months)



    }
}