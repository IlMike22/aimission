package com.example.michl.aimission.GoalsScene

import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Month
import com.google.firebase.database.DataSnapshot

interface IGoalsInteractor {

    fun getGoals(userId: String, data: DataSnapshot, monthItem: Month)
    fun changeGoalProgress(goal: Goal?, position: Int)
    fun storeGoalsInSharedPreferences(goals: ArrayList<Goal>)
    fun getGoalInformationFromSharedPrefs(month: Int, year: Int)
    fun updateGoals()
}