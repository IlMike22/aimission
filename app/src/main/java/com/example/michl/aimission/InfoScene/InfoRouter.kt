package com.example.michl.aimission.InfoScene

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.util.Log
import com.example.michl.aimission.InfoScene.Views.InfoActivity
import com.example.michl.aimission.InfoScene.Views.InfoFragment
import com.example.michl.aimission.RegisterScene.Views.RegisterActivity
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

const val REQUEST_USER_REGISTER_SUCCEED = 1

interface InfoRouterInput {
    fun openRegisterView(activity:Activity)
}

class InfoRouter : InfoRouterInput {
    var fragment: WeakReference<InfoFragment>? = null


    override fun openRegisterView(activity: Activity) {
        Aimission.getAppContext()?.apply {
            var intent = Intent(Aimission.getAppContext(),RegisterActivity::class.java)
            activity.startActivityForResult(intent, REQUEST_USER_REGISTER_SUCCEED) // todo in docu we dont need activity as parameter but here it has to be. why?
        }?: Log.e(TAG, "Context is null. Cannot load activity RegisterActivity")
    }
}