package com.example.michl.aimission.utitlity

import androidx.room.TypeConverter
import com.example.michl.aimission.models.Genre
import com.example.michl.aimission.models.Status

class DefaultGoalsTypeConverter {
    @TypeConverter
    fun GenreToInt(genre: Genre) =
            genre.value

    @TypeConverter
    fun IntToGenre(value: Int) =
            when (value) {
                0 -> Genre.PRIVATE
                1 -> Genre.WORK
                2 -> Genre.HEALTH
                3 -> Genre.FINANCES
                4 -> Genre.EDUCATION
                5 -> Genre.FUN
                else -> Genre.UNDEFINED
            }

    @TypeConverter
    fun StatusToInt(status: Status) =
            status.value

    @TypeConverter
    fun IntToStatus(value: Int) =
            when (value) {
                0 -> Status.OPEN
                1 -> Status.PROGRESS
                2 -> Status.DONE
                else -> Status.UNDEFINED
            }
}