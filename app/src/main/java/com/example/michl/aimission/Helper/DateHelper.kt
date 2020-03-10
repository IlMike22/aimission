package com.example.michl.aimission.Helper

import android.util.Log
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.Models.getMonthName
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import java.time.LocalDate

class DateHelper {

    companion object DateHelper {

        var currentDate = LocalDate.now()

        fun getCurrentMonth(): Int {
            return currentDate.month.value
        }

        fun getCurrentYear(): Int {
            return currentDate.year
        }

        fun convertDataInAimItem(data: DataSnapshot, queryMonth: Int? = null, queryYear: Int? = null): ArrayList<AimItem> {
            var aimList = ArrayList<AimItem>()

            try {
                for (singleDataSet in data.children) {
                    singleDataSet.getValue(AimItem::class.java)?.apply {
                        queryYear?.let { year ->
                            queryMonth?.let { month ->
                                if (this.year == year && this.month == month)
                                    aimList.add(this)

                            }
                        } ?: aimList.add(this)
                    }
                }

            } catch (exc: Exception) {
                Log.e(TAG, "Error while trying to convert db data into AimItem. ${exc.message}")
            }

            return aimList
        }
    }
}

//fun getMonthItem(month:Int, year:Int):MonthItem {
//
//}

enum class MODE_SELECTOR {
    Edit,
    Create
}





