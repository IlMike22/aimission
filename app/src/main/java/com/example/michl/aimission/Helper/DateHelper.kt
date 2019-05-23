package com.example.michl.aimission.Helper

import android.util.Log
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import java.time.LocalDate

class DateHelper {

    companion object DateHelper {

        var currentDate = LocalDate.now()

        fun getCurrentMonth(): Month {
            return getMonthItem(currentDate.month.value)
        }

        fun getCurrentYear(): Int {
            return currentDate.year
        }

        fun convertDataInAimItem(data: DataSnapshot, queryMonth: Month? = null, queryYear: Int? = null): ArrayList<AimItem> {
            var aimList = ArrayList<AimItem>()

            try {
                for (singleDataSet in data.children) {
                        singleDataSet.getValue(AimItem::class.java)?.apply {
                            queryYear?.let { year ->
                                queryMonth?.let { month ->
                                    if (this.year == year && this.month == getIntFromMonth(month))
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

enum class MODE_SELECTOR {
    Read,
    Edit,
    Create
}

fun getMonthItem(month:Int?):Month
{
    return when (month)
    {
        0 -> Month.UNKNOWN
        1 -> Month.JANUARY
        2 -> Month.FEBRUARY
        3 -> Month.MARCH
        4 -> Month.APRIL
        5 -> Month.MAY
        6 -> Month.JUNE
        7 -> Month.JULY
        8 -> Month.AUGUST
        9 -> Month.SEPTEMBER
        10 -> Month.OCTOBER
        11 -> Month.NOVEMBER
        12 -> Month.DECEMBER
        else -> Month.UNKNOWN
    }
}

fun getIntFromMonth(month:Month):Int
{
    return when (month)
    {
        Month.UNKNOWN -> 0
        Month.JANUARY -> 1
        Month.FEBRUARY -> 2
        Month.MARCH -> 3
        Month.APRIL -> 4
        Month.MAY -> 5
        Month.JUNE -> 6
        Month.JULY -> 7
        Month.AUGUST -> 8
        Month.SEPTEMBER -> 9
        Month.OCTOBER -> 10
        Month.NOVEMBER -> 11
        Month.DECEMBER -> 12
    }
}

fun getMonthAsText(month:Month):String
{
    return when(month)
    {
        Month.UNKNOWN -> "Unbekannt"
        Month.JANUARY -> "Januar"
        Month.FEBRUARY -> "Februar"
        Month.MARCH -> "März"
        Month.APRIL -> "April"
        Month.MAY -> "Mai"
        Month.JUNE -> "Juni"
        Month.JULY -> "Juli"
        Month.AUGUST -> "August"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "Oktober"
        Month.NOVEMBER -> "November"
        Month.DECEMBER -> "Dezember"
    }
}

fun getCurrentUserId(): String {
    return FirebaseAuth.getInstance().currentUser?.uid ?: ""
}



