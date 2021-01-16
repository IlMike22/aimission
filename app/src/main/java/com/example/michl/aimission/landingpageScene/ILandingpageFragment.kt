package com.example.michl.aimission.landingpageScene

import com.example.michl.aimission.models.Month

interface ILandingpageFragment {
    fun afterUserIdNotFound(errorMsg: String)
    fun afterMonthsLoadedSuccessfully(months: ArrayList<Month>)
    fun afterMonthItemsLoadedError(errorMsg: String)
    fun afterEmptyMonthListLoaded(msg: String, month: Month)
}