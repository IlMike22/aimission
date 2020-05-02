package com.example.michl.aimission.LandingpageScene

import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Month
import java.lang.ref.WeakReference


class LandingpagePresenter : ILandingpagePresenter {
    var output: WeakReference<ILandingpageFragment>? = null

    override fun onNoUserIdExists() {
        val errorMsg = "Couldn't find user id. Are you logged in?"
        output?.get()?.afterUserIdNotFound(errorMsg)
    }

    override fun onMonthsLoaded(aims: ArrayList<Goal?>, months: ArrayList<Month>) {
        output?.get()?.afterMonthItemsLoadedSuccessfully(sortMonths(months))
    }

    override fun onMonthsLoadedFailed(errorMsg: String) {
        output?.get()?.afterMonthItemsLoadedFailed(errorMsg)
    }

    override fun onEmptyMonthsLoaded(month: Month) {
        val msg = "At the moment there are no aims defined by you. Create now your first aim."
        output?.get()?.afterEmptyMonthListLoaded(msg, month)
    }

    private fun sortMonths(months: ArrayList<Month>): ArrayList<Month> {
        months.sortByDescending { month ->
            month.month
        }
        months.sortByDescending { month ->
            month.year
        }

        return months
    }
}