package com.example.michl.aimission.AimListScene

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.AimDetailScene.Views.AimDetailActivity
import com.example.michl.aimission.AimListScene.Views.AimListFragment
import com.example.michl.aimission.Helper.MODE_SELECTOR
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import java.lang.ref.WeakReference


interface AimListRouterInput {
    fun showAimDetailView(aimId: String, mode: MODE_SELECTOR)
}

class AimListRouter : AimListRouterInput {
    var fragment: WeakReference<AimListFragment>? = null

    override fun showAimDetailView(aimId: String, mode: MODE_SELECTOR) {
        var intent = Intent(Aimission.getAppContext(), AimDetailActivity::class.java)
        if (aimId.isEmpty())
        {
            Log.e(TAG, "Couldnt open aim detail view. Id from list is empty.")
            return
        }

        intent.putExtra("AimId", aimId)
        intent.putExtra("Mode", mode)
        Aimission.getAppContext()?.apply {
            startActivity(intent)
        }

    }
}