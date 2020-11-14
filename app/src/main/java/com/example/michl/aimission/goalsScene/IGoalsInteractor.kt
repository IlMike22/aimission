package com.example.michl.aimission.goalsScene

import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.models.Month
import com.google.firebase.database.DataSnapshot

interface IGoalsInteractor {

    fun getGoals(userId: String, data: DataSnapshot, monthItem: Month)
    fun changeGoalProgress(goal: Goal?, position: Int)
    fun storeGoalsInSharedPreferences(goals: ArrayList<Goal>)
    fun getGoalInformationFromSharedPrefs(month: Int, year: Int)
    fun updateGoals()
}