package com.example.michl.aimission.Helper

import android.util.Log
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.database.DataSnapshot
import java.time.LocalDate

class DateHelper {

    companion object DateHelper {

        var current = LocalDate.now()

        fun getCurrentMonth(): Int {
            return current.month.value
        }

        fun getCurrentYear(): Int {
            return current.year
        }

        fun convertDataInAimItem(data: DataSnapshot, queryMonth: Int? = null, queryYear: Int? = null): ArrayList<AimItem> {
            var aimList = ArrayList<AimItem>()

            try {
                for (singleDataSet in data.children) {
                    var result = AimItem()
                    result.id = singleDataSet.child("id").value as String?
                    result.title = singleDataSet.child("title").value as String?
                    result.description = singleDataSet.child("description").value as String?
                    result.genre = singleDataSet.child("genre").value as Genre?
                    result.isHighPriority = singleDataSet.child("highPriority").value as Boolean?
                    result.month = singleDataSet.child("month").value as Int?
                    result.year = singleDataSet.child("year").value as Int?
                    result.repeatCount = singleDataSet.child("repeatCount").value as Int?
                    result.status = singleDataSet.child("status").value as Status?

                    queryYear?.let { year ->
                        queryMonth?.let { month ->
                            if (result.year == year && result.month == month)
                                aimList.add(result)

                        }
                    } ?: aimList.add(result)
                }

            } catch (exc: Exception) {
                Log.e(TAG, "Error while trying to convert db data into AimItem. ${exc.message}")
            }

            return aimList
        }
    }
}
