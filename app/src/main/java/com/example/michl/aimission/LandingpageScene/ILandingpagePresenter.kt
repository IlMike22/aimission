package com.example.michl.aimission.LandingpageScene

import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Month

interface ILandingpagePresenter {
    fun onNoUserIdExists()
    fun onMonthsLoaded(goals: ArrayList<Goal?>, months: ArrayList<Month>)
    fun onMonthsLoadedFailed(errorMsg: String)
    fun onEmptyMonthsLoaded(month: Month)

}