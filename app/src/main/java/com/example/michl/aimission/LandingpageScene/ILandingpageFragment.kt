package com.example.michl.aimission.LandingpageScene

import com.example.michl.aimission.Models.MonthItem


interface ILandingpageFragment {
    fun afterUserIdNotFound(errorMsg: String)
    fun afterMonthItemsLoadedSuccessfully(monthItems: ArrayList<MonthItem>)
    fun afterMonthItemsLoadedFailed(errorMsg: String)
    fun afterEmptyMonthListLoaded(msg: String, firstItem: MonthItem)
}