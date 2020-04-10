package com.example.michl.aimission.LandingpageScene

import android.content.Context

interface ILandingpageRouter {
    fun openAimListView(context: Context, month: Int, year: Int)
}