package com.example.michl.aimission.MainScene.Views

import com.example.michl.aimission.Base.BaseActivity
import com.example.michl.aimission.R

class MainActivity : BaseActivity() {

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_home
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }
}
