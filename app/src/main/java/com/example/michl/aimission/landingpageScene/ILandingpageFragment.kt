package com.example.michl.aimission.landingpageScene

import com.example.michl.aimission.models.Month

interface ILandingpageFragment {
    fun afterUserIdNotFound(errorMsg: String)
    fun afterMonthItemsLoadedSuccessfully(months: ArrayList<Month>)
    fun afterMonthItemsLoadedFailed(errorMsg: String)
    fun afterEmptyMonthListLoaded(msg: String, month: Month)
}