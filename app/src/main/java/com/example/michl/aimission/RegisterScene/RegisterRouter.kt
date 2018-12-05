package com.example.michl.aimission.RegisterScene

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.InfoScene.Views.InfoActivity
import com.example.michl.aimission.InfoScene.Views.InfoFragment
import com.example.michl.aimission.RegisterScene.Views.RegisterActivity
import com.example.michl.aimission.RegisterScene.Views.RegisterFragment
import com.example.michl.aimission.Utility.Aimission
import java.lang.ref.WeakReference

interface RegisterRouterInput
{
    fun navigateToInfo()
}

class RegisterRouter:RegisterRouterInput
{
    var fragment: WeakReference<RegisterFragment>? = null
    val TAG = "Aimission"

    override fun navigateToInfo() {


        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), InfoActivity::class.java))
        }?: Log.e(TAG, "Context is null. Cannot load activity InfoActivity")
    }
}