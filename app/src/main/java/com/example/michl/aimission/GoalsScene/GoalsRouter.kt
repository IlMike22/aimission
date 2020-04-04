package com.example.michl.aimission.GoalsScene

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.util.Log
import com.example.michl.aimission.GoalDetailScene.Views.AimDetailActivity
import com.example.michl.aimission.GoalsScene.Views.GoalsFragment
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference


interface AimListRouterInput {
    fun showAimDetailView(aimId: String, mode: DateHelper.MODE_SELECTOR, sourceActivity: Activity?=null)
}

class GoalsRouter : AimListRouterInput {
    var fragment: WeakReference<GoalsFragment>? = null
    val REQUEST_UPDATE_LIST = 101

    override fun showAimDetailView(aimId: String, mode: DateHelper.MODE_SELECTOR, sourceActivity:Activity?) {
        var intent = Intent(Aimission.getAppContext(), AimDetailActivity::class.java)

        if (aimId.isEmpty() && mode != DateHelper.MODE_SELECTOR.Create)
        {
            Log.e(TAG, "Couldn't open aim detail view. Id from list is empty.")
            return
        }

        intent.putExtra("AimId", aimId)
        intent.putExtra("Mode", mode)
        Aimission.getAppContext()?.apply {
            sourceActivity?.apply {
                startActivityForResult(this,intent, REQUEST_UPDATE_LIST,null)
            }?:Log.i(TAG,"Activity is null. Cannot call startActivityForResult in AimListRouter")
        }
    }
}