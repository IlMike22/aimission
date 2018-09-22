package com.example.michl.aimission.Base

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.InfoScene.Views.InfoActivity
import com.example.michl.aimission.SettingsScene.Views.SettingsActivity
import com.example.michl.aimission.Utility.Aimission

interface BaseRouterInput {
    fun openSettingsView()
    fun openInfoView()
    fun openMainView()
}


class BaseRouter : BaseRouterInput {
    override fun openSettingsView() {
        Log.i("michl", "open settings view")
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), SettingsActivity::class.java))
        } ?: Log.i("michl", "Cxt is null. Cannot route to next act")
    }

    override fun openInfoView() {
        Log.i("michl", "open info view")
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), InfoActivity::class.java))
        } ?: Log.i("michl", "Cxt is null. Cannot route to next act")
    }

    override fun openMainView() {
        Log.i("michl", "open info view")

    }
}
