package com.example.michl.aimission.landingpageScene

import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.models.Month

interface ILandingpagePresenter {
    fun onNoUserIdExists()

    fun onMonthsLoaded(goals: ArrayList<Goal?>, months: ArrayList<Month>)

    fun onMonthsLoadedFailed(errorMsg: String)

    fun onEmptyMonthsLoaded(month: Month)

}