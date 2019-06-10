package com.example.michl.aimission.Models

data class MonthItem(
        var name: String,
        var aimsAmount: Int,
        var aimsSucceeded: Int,
        var month: Month,
        var year: Int)


enum class Month {
    JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, UNKNOWN
}

fun ArrayList<MonthItem>.containsMonthItem(month: Month, year: Int): Boolean {
    for (item in this) {
        if (item.month == month && item.year == year)
            return true
    }

    return false
}
