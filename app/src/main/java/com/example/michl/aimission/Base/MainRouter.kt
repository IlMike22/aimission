package com.example.michl.aimission.Base

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.michl.aimission.GoalScene.views.GoalFragment
import com.example.michl.aimission.InfoScene.views.InfoFragment
import com.example.michl.aimission.LandingpageScene.views.LandingpageFragment
import com.example.michl.aimission.R
import com.example.michl.aimission.SettingsScene.views.SettingsFragment
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

interface MainRouterInput {
    fun openSettingsView(manager: FragmentManager)
    fun openInfoView(manager: FragmentManager)
    fun openMainView(manager: FragmentManager)
    fun openAimDetailView(manager: FragmentManager)
}


class MainRouter : MainRouterInput {

    var fragment: WeakReference<LandingpageFragment>? = null

    override fun openAimDetailView(manager: FragmentManager) {
        Log.i(TAG,"open detail view router called.")
        manager.beginTransaction().replace(R.id.fragmentContainer, GoalFragment()).addToBackStack(null).commitAllowingStateLoss()
    }

    override fun openSettingsView(manager: FragmentManager) {
        Log.i(TAG, "open settings view")
        manager.beginTransaction().replace(R.id.fragmentContainer, SettingsFragment()).addToBackStack(null).commitAllowingStateLoss()
    }

    override fun openInfoView(manager: FragmentManager) {
        Log.i(TAG, "open info view")
        manager.beginTransaction().replace(R.id.fragmentContainer, InfoFragment()).addToBackStack(null).commitAllowingStateLoss()
    }

    override fun openMainView(manager: FragmentManager) {
        Log.i(TAG, "open main view")
        manager.beginTransaction().replace(R.id.fragmentContainer, LandingpageFragment()).addToBackStack(null).commitAllowingStateLoss()
    }
}
