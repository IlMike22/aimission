package com.example.michl.aimission.utitlity

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.michl.aimission.models.Goal

@Database(entities = [Goal::class], version = 1)
@TypeConverters(DefaultGoalsTypeConverter::class)
abstract class DefaultGoalsDatabase : RoomDatabase() {
    abstract fun DefaultGoalsDao(): DefaultGoalsDao
}