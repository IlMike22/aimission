package com.example.michl.aimission.landingpageScene.implementation

import android.content.Context
import com.example.michl.aimission.landingpageScene.ILandingpageFragment
import com.example.michl.aimission.landingpageScene.ILandingpagePresenter
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.models.Month
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
        output?.get()?.afterMonthsLoadedSuccessfully(sortMonths(months))
    }

    override fun onMonthsLoadedFailed(errorMsg: String) {
        output?.get()?.afterMonthItemsLoadedError(errorMsg)
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