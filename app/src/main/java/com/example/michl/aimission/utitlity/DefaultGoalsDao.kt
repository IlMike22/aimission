package com.example.michl.aimission.utitlity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.michl.aimission.models.Goal

@Dao
interface DefaultGoalsDao {
    @Query("SELECT * FROM default_goals")
    fun getDefaultGoalsFromRoomDb(): List<Goal>

    @Query("SELECT COUNT(id) FROM default_goals")
    fun getDefaultGoalAmount():Int

    @Insert
    fun storeDefaultGoalToRoomDb(vararg goals: Goal)

    @Delete
    fun removeDefaultGoalFromRoomDb(goal:Goal)
}