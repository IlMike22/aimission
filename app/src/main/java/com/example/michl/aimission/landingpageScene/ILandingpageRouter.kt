package com.example.michl.aimission.landingpageScene

import android.content.Context
import com.example.michl.aimission.models.Month

interface ILandingpageRouter {
    fun openGoals(
            context: Context,
            month: Month
    )
}