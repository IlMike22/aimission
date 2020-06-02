package com.example.michl.aimission.LandingpageScene

import android.content.Context
import com.example.michl.aimission.Models.Month

interface ILandingpageRouter {
    fun openGoals(
            context: Context,
            month: Month
    )
}