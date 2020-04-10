package com.example.michl.aimission.LandingpageScene

import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.MonthItem
import java.lang.ref.WeakReference


class LandingpagePresenter : ILandingpagePresenter {
    var output: WeakReference<ILandingpageFragment>? = null

    override fun onNoUserIdExists() {
        val errorMsg = "Couldn't find user id. Are you logged in?"
        output?.get()?.afterUserIdNotFound(errorMsg)
    }

    override fun onMonthsLoaded(aims: ArrayList<Goal?>, months: ArrayList<MonthItem>) {
        output?.get()?.afterMonthItemsLoadedSuccessfully(sortMonths(months))
    }

    override fun onMonthsLoadedFailed(errorMsg: String) {
        output?.get()?.afterMonthItemsLoadedFailed(errorMsg)
    }

    override fun onEmptyMonthsLoaded(firstItem: MonthItem) {
        val msg = "At the moment there are no aims defined by you. Create now your first aim."
        output?.get()?.afterEmptyMonthListLoaded(msg, firstItem)
    }

    private fun sortMonths(months: ArrayList<MonthItem>): ArrayList<MonthItem> {
        months.sortByDescending { month ->
            month.month
        }
        months.sortByDescending { month ->
            month.year
        }

        return months
    }
}