package com.example.michl.aimission.MainScene

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.AimListScene.Views.AimListActivity
import com.example.michl.aimission.InfoScene.Views.InfoActivity
import com.example.michl.aimission.SettingsScene.Views.SettingsActivity
import com.example.michl.aimission.Utility.Aimission

interface MainRouterInput {
    fun openMonthItemDetails()
    fun openSettingsView()
    fun openInfoView()
    fun openMainView()
}

class MainRouter : MainRouterInput {
    override fun openSettingsView() {
        Log.i("michl","open settings view")
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(),SettingsActivity::class.java))
        }?: Log.i("michl","Cxt is null. Cannot route to next act")
    }

    override fun openInfoView() {
        Log.i("michl","open info view")
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(),InfoActivity::class.java))
        }?: Log.i("michl","Cxt is null. Cannot route to next act")
    }

    override fun openMainView() {
        Log.i("michl","open info view")

    }

    override fun openMonthItemDetails() {
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), AimListActivity::class.java))
        } ?: Log.i("michl", "Context is null. Cannot route to next act")
    }




}