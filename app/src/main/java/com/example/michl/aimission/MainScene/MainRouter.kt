package com.example.michl.aimission.MainScene

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.AimListScene.Views.AimListActivity
import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Utility.Aimission
import java.lang.ref.WeakReference

interface MainRouterInput {
    fun openAimListView(month: Month, year: Int)
}

class MainRouter : MainRouterInput {

    var fragment: WeakReference<MainFragment>? = null

    override fun openAimListView(month: Month, year: Int) {
        Aimission.getAppContext()?.apply {
            var intent = Intent(Aimission.getAppContext(), AimListActivity::class.java)
            intent.putExtra("month", month)
            intent.putExtra("year", year)
            startActivity(intent)

        } ?: Log.i("michl", "Context is null. Cannot route to next act")
    }

}