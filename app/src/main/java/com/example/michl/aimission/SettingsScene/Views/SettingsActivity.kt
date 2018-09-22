package com.example.michl.aimission.SettingsScene.Views

import android.os.Bundle
import com.example.michl.aimission.Base.BaseActivity
import com.example.michl.aimission.R

class SettingsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_settings
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_settings
    }
}
