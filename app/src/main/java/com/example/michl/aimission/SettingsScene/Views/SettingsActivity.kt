package com.example.michl.aimission.SettingsScene.Views

import android.os.Bundle
import android.view.MenuItem
import com.example.michl.aimission.Base.BaseActivity
import com.example.michl.aimission.R

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

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
