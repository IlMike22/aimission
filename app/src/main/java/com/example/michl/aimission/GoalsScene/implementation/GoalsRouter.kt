package com.example.michl.aimission.GoalsScene.implementation

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.michl.aimission.GoalScene.views.GoalActivity
import com.example.michl.aimission.GoalsScene.views.GoalsFragment
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference


interface AimListRouterInput {
    fun showGoalDetail(aimId: String, mode: DateHelper.MODE_SELECTOR, sourceActivity: Activity?=null)
}

class GoalsRouter : AimListRouterInput {
    var fragment: WeakReference<GoalsFragment>? = null
    val REQUEST_UPDATE_LIST = 101

    override fun showGoalDetail(aimId: String, mode: DateHelper.MODE_SELECTOR, sourceActivity:Activity?) {
        var intent = Intent(Aimission.getAppContext(), GoalActivity::class.java)

        if (aimId.isEmpty() && mode != DateHelper.MODE_SELECTOR.Create)
        {
            Log.e(TAG, "Couldn't open aim detail view. Id from list is empty.")
            return
        }

        intent.putExtra("AimId", aimId)
        intent.putExtra("Mode", mode)
        Aimission.getAppContext()?.apply {
            sourceActivity?.apply {
                startActivityForResult(intent, REQUEST_UPDATE_LIST)
            }?:Log.i(TAG,"Activity is null. Cannot call startActivityForResult in AimListRouter")
        }
    }
}