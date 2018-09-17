package com.example.michl.aimission.MainScene

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.AimListScene.Views.AimListActivity
import com.example.michl.aimission.Utility.Aimission

interface MainRouterInput {
    fun openMonthItemDetails()
}

class MainRouter : MainRouterInput {
    override fun openMonthItemDetails() {
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), AimListActivity::class.java))
        } ?: Log.i("michl", "Context is null. Cannot route to next act")
    }


}