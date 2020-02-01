package com.example.michl.aimission.Models

data class MonthItem(
        var name: String,
        var aimsAmount: Int,
        var aimsSucceeded: Int,
        var month: Int,
        var year: Int,
        var isFirstStart:Boolean)


fun ArrayList<MonthItem>.containsMonthItem(month: Int, year: Int): Boolean {
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
