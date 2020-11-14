package com.example.michl.aimission.registerScene.implementation

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.michl.aimission.infoScene.views.InfoActivity
import com.example.michl.aimission.R
import com.example.michl.aimission.registerScene.views.RegisterFragment
import com.example.michl.aimission.utitlity.Aimission
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import java.lang.ref.WeakReference

interface RegisterRouterInput
{
    fun navigateToInfo()
}

class RegisterRouter(val context: Context?): RegisterRouterInput
{
    var fragment: WeakReference<RegisterFragment>? = null

    override fun navigateToInfo() {
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), InfoActivity::class.java))
        }?: Log.e(TAG, context?.getString(R.string.register_error_context_is_null))
    }
}