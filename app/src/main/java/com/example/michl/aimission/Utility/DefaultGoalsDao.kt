package com.example.michl.aimission.Utility

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.michl.aimission.Models.Goal

@Dao
interface DefaultGoalsDao {
    @Query("SELECT * FROM monthItem")
    fun getDefaultGoalsFromRoomDb(): ArrayList<Goal>

    @Insert
    fun storeDefaultGoalsToRoomDb(vararg goals: ArrayList<Goal>)

    @Delete
    fun removeDefaultGoalFromRoomDb(goal:Goal)

    companion object {

    }
}