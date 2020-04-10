package com.example.michl.aimission.LandingpageScene

import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.MonthItem

interface ILandingpagePresenter {
    fun onNoUserIdExists()
    fun onMonthsLoaded(goals: ArrayList<Goal?>, monthItems: ArrayList<MonthItem>)
    fun onMonthsLoadedFailed(errorMsg: String)
    fun onEmptyMonthsLoaded(firstItem: MonthItem)

}