package com.example.michl.aimission.Models

data class AimItem(
        var id: String? = null,     // this should be the id which firebase uses to save this item in table aims
        var title: String? = null,
        var description: String? = null,
        var repeatCount: Int? = null, // defines if an item has more than one part-steps/aims
        var highPriority: Boolean? = null,
        var status: Status? = null,
        var genre: Genre? = null,
        var month: Int? = null,
        var year: Int? = null,
        var comesBack: Boolean? = null // defines if an item comes back every new month

)

enum class Status {
    OPEN, PROGRESS, DONE, UNDEFINED
}

enum class Genre {
    PRIVATE, WORK, HEALTH, FINANCES, EDUCATION, FUN, UNDEFINED
}