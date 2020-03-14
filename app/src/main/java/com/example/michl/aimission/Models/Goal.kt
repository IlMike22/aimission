package com.example.michl.aimission.Models

import java.time.LocalDateTime
import java.util.*

data class Goal(
        var id: String = "",     // this should be the id which firebase uses to save this item in table aims
        var title: String = "",
        var description: String = "",
        var creationDate: String = "",
        var repeatCount: Int = 0, // defines if an item has more than one part-steps/aims
        var isHighPriority: Boolean = false,
        var isRepetitive: Boolean = false,
        var status: Status = Status.UNDEFINED,
        var genre: Genre = Genre.UNDEFINED,
        var month: Int = 0,
        var year: Int = 0,
        var isComingBack: Boolean = false // defines if an item comes back every new month
)

enum class Status {
    OPEN, PROGRESS, DONE, UNDEFINED
}

enum class Genre {
    PRIVATE, WORK, HEALTH, FINANCES, EDUCATION, FUN, UNDEFINED
}