package com.example.michl.aimission.Models

data class AimItem(
        var id:String,     // this should be the id which firebase uses to save this item in table aims
        var title:String,
        var genre:Int, // todo use later a enum with specified genre types like "fun, work, etc."
        var description:String,
        var priority:Int, // todo use later a enum with specified priority level
        var type: Int,  // todo use later a enum with specified type (aim can be marked as done, or has a specified count)
        var repeatCount:Int // if aim is marked as a repeated aim, save here the amount of numbers this aim should be absolved
)