package com.example.michl.aimission.LandingpageScene

import com.example.michl.aimission.Models.Month


interface ILandingpageFragment {
    fun afterUserIdNotFound(errorMsg: String)
    fun afterMonthItemsLoadedSuccessfully(months: ArrayList<Month>)
    fun afterMonthItemsLoadedFailed(errorMsg: String)
    fun afterEmptyMonthListLoaded(msg: String, month: Month)
}