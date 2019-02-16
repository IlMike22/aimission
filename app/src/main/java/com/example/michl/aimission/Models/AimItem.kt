package com.example.michl.aimission.Models

data class AimItem(
        var id:String,     // this should be the id which firebase uses to save this item in table aims
        var title:String,
        var description:String,
        var repeatCount:Long, // if aim is marked as a repeated aim, save here the amount of numbers this aim should be absolved
        var isHighPriority:Boolean,
        var status: Status,
        var genre:Genre,
        var month: Int,
        var year: Int

)

enum class Status
{
    OPEN,PROGRESS,DONE
}

enum class Genre
{
    PRIVATE,WORK,HEALTH,FINANCES,EDUCATION,FUN
}

enum class Month
{
    JANUARY, FEBRURARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
}