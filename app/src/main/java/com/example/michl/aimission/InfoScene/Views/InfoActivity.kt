package com.example.michl.aimission.InfoScene.Views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.michl.aimission.Base.BaseActivity
import com.example.michl.aimission.R

class InfoActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_info
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_info
    }
}
