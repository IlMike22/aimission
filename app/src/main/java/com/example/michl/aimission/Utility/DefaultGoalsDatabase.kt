package com.example.michl.aimission.Utility

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.michl.aimission.Models.Goal

@Database(entities = [Goal::class], version = 1)
@TypeConverters(DefaultGoalsTypeConverter::class)
abstract class DefaultGoalsDatabase : RoomDatabase() {
    abstract fun DefaultGoalsDao(): DefaultGoalsDao
}