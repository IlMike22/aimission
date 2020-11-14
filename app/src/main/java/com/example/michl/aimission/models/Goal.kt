package com.example.michl.aimission.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "default_goals")
data class Goal(
        @PrimaryKey var id: String = "",     // this should be the id which firebase uses to save this item in table goals
        var title: String = "",
        var description: String = "",
        @ColumnInfo(name = "creation_date") var creationDate: String = "",
        @ColumnInfo(name = "repeat_count")var repeatCount: Int = 0, // defines if an item has more than one part-steps/goals
        @ColumnInfo(name = "part_goals_achieved") var partGoalsAchieved:Int = 0, // a goal which has to be achieved several times in a month
        @ColumnInfo(name = "is_high_priority")var isHighPriority: Boolean = false,
        var status: Status = Status.UNDEFINED,
        var genre: Genre = Genre.UNDEFINED,
        var month: Int = 0,
        var year: Int = 0,
        @ColumnInfo(name = "is_coming_back") var isComingBack: Boolean = false // defines if an item comes back every new month

)

enum class Status(val value:Int) {
    OPEN(0), PROGRESS(1), DONE(2), UNDEFINED(-1)
}

enum class Genre(val value: Int) {
    PRIVATE(0), WORK(1), HEALTH(2), FINANCES(3), EDUCATION(4), FUN(5), UNDEFINED(-1)
}