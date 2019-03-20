package com.example.michl.aimission.Models

data class AimItem(
        var id:String?=null,     // this should be the id which firebase uses to save this item in table aims
        var title:String?=null,
        var description:String?=null,
        var repeatCount:Int?=null, // if aim is marked as a repeated aim, save here the amount of numbers this aim should be absolved
        var highPriority:Boolean?=null,
        var status: Status?=null,
        var genre:Genre?=null,
        var month: Int?=null,
        var year: Int?=null

)

enum class Status
{
    OPEN,PROGRESS,DONE, UNDEFINED
}

enum class Genre
{
    PRIVATE,WORK,HEALTH,FINANCES,EDUCATION,FUN
}