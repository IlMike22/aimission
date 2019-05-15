package com.example.michl.aimission.Models

data class MonthItem(
        var name:String,
        var aimsAmount:Int,
        var aimsSucceeded:Int,
        var month:Month,
        var year:Int)


enum class Month
{
    JANUARY,FEBRUARY,MARCH,APRIL,MAY,JUNE,JULY,AUGUST,SEPTEMBER,NOVEMBER,DECEMBER
}
