package com.example.michl.aimission.Base

import android.support.v4.app.FragmentManager
import android.util.Log
import com.example.michl.aimission.InfoScene.Views.InfoFragment
import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.R
import com.example.michl.aimission.SettingsScene.Views.SettingsFragment

interface MainRouterInput {
    fun openSettingsView(manager: FragmentManager)
    fun openInfoView(manager: FragmentManager)
    fun openMainView(manager: FragmentManager)
}


class MainRouter : MainRouterInput {
    override fun openSettingsView(manager: FragmentManager) {
        Log.i("mike", "open settings view")
        manager.beginTransaction().replace(R.id.fragmentContainer, SettingsFragment()).addToBackStack(null).commit()
    }

    override fun openInfoView(manager: FragmentManager) {
        Log.i("mike", "open info view")
        manager.beginTransaction().replace(R.id.fragmentContainer, InfoFragment()).addToBackStack(null).commit()
    }

    override fun openMainView(manager: FragmentManager) {
        Log.i("mike", "open main view")
        manager.beginTransaction().replace(R.id.fragmentContainer, MainFragment()).addToBackStack(null).commit()
    }
}
