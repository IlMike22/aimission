package com.example.michl.aimission.Utility

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.michl.aimission.Models.Goal

@Database(entities = [Goal::class], version = 1)
abstract class DefaultGoalsDatabase : RoomDatabase() {
    abstract fun queryDefaultGoalsDao(): DefaultGoalsDao
}