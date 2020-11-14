package com.example.michl.aimission.infoScene.views

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import com.example.michl.aimission.base.BaseActivity
import com.example.michl.aimission.R

class InfoActivity : BaseActivity() {



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("Aimission","now we are here.")
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_info
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_info
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
