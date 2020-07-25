package com.example.michl.aimission.RegisterScene.implementation

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.InfoScene.views.InfoActivity
import com.example.michl.aimission.RegisterScene.views.RegisterFragment
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

interface RegisterRouterInput
{
    fun navigateToInfo()
}

class RegisterRouter: RegisterRouterInput
{
    var fragment: WeakReference<RegisterFragment>? = null

    override fun navigateToInfo() {
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), InfoActivity::class.java))
        }?: Log.e(TAG, "Context is null. Cannot load activity InfoActivity")
    }
}