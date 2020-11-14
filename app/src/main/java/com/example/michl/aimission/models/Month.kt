package com.example.michl.aimission.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "monthItem")
data class Month(
        var name: String,
        var goalAmount: Int,
        var goalsCompleted: Int,
        var month: Int,
        var year: Int,
        var isFirstStart:Boolean,
        var isDepecrecated:Boolean
):Parcelable

fun ArrayList<Month>.containsMonth(month: Int, year: Int): Boolean {
    for (item in this) {
        if (item.month == month && item.year == year)
            return true
    }

    return false
}

fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> "Unknown"
    }
}
