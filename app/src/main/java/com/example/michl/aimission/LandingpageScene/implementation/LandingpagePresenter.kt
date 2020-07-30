package com.example.michl.aimission.LandingpageScene.implementation

import android.content.Context
import com.example.michl.aimission.LandingpageScene.ILandingpageFragment
import com.example.michl.aimission.LandingpageScene.ILandingpagePresenter
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.R
import java.lang.ref.WeakReference

class LandingpagePresenter(val context: Context?) : ILandingpagePresenter {
    var output: WeakReference<ILandingpageFragment>? = null

    override fun onNoUserIdExists() {
        context?.apply {
            val errorMsg = getString(R.string.landing_page_no_user_id_found)
            output?.get()?.afterUserIdNotFound(errorMsg)
        }
    }

    override fun onMonthsLoaded(goals: ArrayList<Goal?>, months: ArrayList<Month>) {
        output?.get()?.afterMonthItemsLoadedSuccessfully(sortMonths(months))
    }

    override fun onMonthsLoadedFailed(errorMsg: String) {
        output?.get()?.afterMonthItemsLoadedFailed(errorMsg)
    }

    override fun onEmptyMonthsLoaded(month: Month) {
        context?.apply {
            val msg = getString(R.string.landing_page_no_goals_defined)
            output?.get()?.afterEmptyMonthListLoaded(msg, month)
        }
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