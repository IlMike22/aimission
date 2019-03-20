package com.example.michl.aimission.Helper

import android.util.Log
import com.example.michl.aimission.Models.AimItem
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
                    for (currentItem in singleDataSet.children) {
                        currentItem.getValue(AimItem::class.java)?.apply {
                            queryYear?.let { year ->
                                queryMonth?.let { month ->
                                    if (this.year == year && this.month == month)
                                        aimList.add(this)

                                }
                            } ?: aimList.add(this)
                        }
                    }
                }

            } catch (exc: Exception) {
                Log.e(TAG, "Error while trying to convert db data into AimItem. ${exc.message}")
            }

            return aimList
        }
    }
}
