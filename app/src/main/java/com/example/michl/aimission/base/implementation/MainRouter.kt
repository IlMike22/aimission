package com.example.michl.aimission.base.implementation

import androidx.fragment.app.FragmentManager
import com.example.michl.aimission.goalScene.views.GoalFragment
import com.example.michl.aimission.infoScene.views.InfoFragment
import com.example.michl.aimission.landingpageScene.views.LandingPageFragment
import com.example.michl.aimission.R
import com.example.michl.aimission.base.IMainRouter
import com.example.michl.aimission.settingScene.views.SettingsFragment
import java.lang.ref.WeakReference

class MainRouter : IMainRouter {
    var fragment: WeakReference<LandingPageFragment>? = null

    override fun openAimDetailView(manager: FragmentManager) {
        manager.beginTransaction().replace(R.id.linear_layout_container_fragment, GoalFragment()).addToBackStack(null).commitAllowingStateLoss()
    }

    override fun openSettingsView(manager: FragmentManager) {
        manager.beginTransaction().replace(R.id.linear_layout_container_fragment, SettingsFragment()).addToBackStack(null).commitAllowingStateLoss()
    }

    override fun openInfoView(manager: FragmentManager) {
        manager.beginTransaction().replace(R.id.linear_layout_container_fragment, InfoFragment()).addToBackStack(null).commitAllowingStateLoss()
    }

    override fun openMainView(manager: FragmentManager) {
        manager.beginTransaction().replace(R.id.linear_layout_container_fragment, LandingPageFragment()).addToBackStack(null).commitAllowingStateLoss()
    }
}
