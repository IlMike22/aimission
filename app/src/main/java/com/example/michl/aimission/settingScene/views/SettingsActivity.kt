package com.example.michl.aimission.settingScene.views

import android.view.MenuItem
import com.example.michl.aimission.base.implementation.BaseActivity
import com.example.michl.aimission.R

class SettingsActivity : BaseActivity() {
    override fun getNavigationMenuItemId(): Int {
        return R.id.action_settings
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_settings
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
