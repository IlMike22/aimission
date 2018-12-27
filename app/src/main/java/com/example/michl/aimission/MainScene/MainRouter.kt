package com.example.michl.aimission.MainScene

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.AimDetailScene.Views.AimDetailActivity
import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.Utility.Aimission
import java.lang.ref.WeakReference

interface MainRouterInput {
    fun openAimDetailView()
}

class MainRouter : MainRouterInput {

    var fragment: WeakReference<MainFragment>? = null

    override fun openAimDetailView() {
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), AimDetailActivity::class.java))
        } ?: Log.i("michl", "Context is null. Cannot route to next act")
    }


}