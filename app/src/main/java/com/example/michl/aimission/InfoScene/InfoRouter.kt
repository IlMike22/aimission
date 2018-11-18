package com.example.michl.aimission.InfoScene

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.InfoScene.Views.InfoFragment
import com.example.michl.aimission.RegisterScene.Views.RegisterActivity
import com.example.michl.aimission.Utility.Aimission
import java.lang.ref.WeakReference


interface InfoRouterInput {
    fun openRegisterView()
}

class InfoRouter : InfoRouterInput {

    var fragment: WeakReference<InfoFragment>? = null
    val TAG = "Aimission"
    //todo implement later

    override fun openRegisterView() {
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(),RegisterActivity::class.java))
        }?: Log.e(TAG, "Context is null. Cannot load activity RegisterActivity")
    }
}