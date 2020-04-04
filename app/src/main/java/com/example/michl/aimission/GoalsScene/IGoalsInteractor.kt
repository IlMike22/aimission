package com.example.michl.aimission.GoalsScene

import com.example.michl.aimission.Models.Goal
import com.google.firebase.database.DataSnapshot

interface IGoalsInteractor {

    fun getGoals(userId: String, data: DataSnapshot, selectedMonth:Int, selectedYear:Int)
    fun changeGoalProgress(goal: Goal?, position: Int)
    fun storeGoalInformationInSharedPrefs(goals: ArrayList<Goal>)
    fun getGoalInformationFromSharedPrefs(month: Int, year: Int)
    fun updateGoals()
}