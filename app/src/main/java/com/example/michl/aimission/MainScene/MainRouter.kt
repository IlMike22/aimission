package com.example.michl.aimission.MainScene

import android.content.Intent
import android.util.Log
import com.example.michl.aimission.AimDetailScene.Views.AimDetailActivity
import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.AimListScene.Views.AimListActivity
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.Utility.Aimission
import java.lang.ref.WeakReference

interface MainRouterInput {
    fun openAimDetailView()
    fun openAimListView(month:Int, year:Int)
}

class MainRouter : MainRouterInput {
    override fun openAimListView(month: Int, year:Int) {
        Aimission.getAppContext()?.apply {
            var intent = Intent(Aimission.getAppContext(), AimListActivity::class.java)
            intent.putExtra("month",month)
            intent.putExtra("year",year)
            startActivity(intent)

        } ?: Log.i("michl", "Context is null. Cannot route to next act")
    }


    var fragment: WeakReference<MainFragment>? = null

    //todo no longer needed in MainRouter because we only open AimListView from MainPage, not DetailView

    override fun openAimDetailView() {
        Aimission.getAppContext()?.apply {
            startActivity(Intent(Aimission.getAppContext(), AimDetailActivity::class.java))
        } ?: Log.i("michl", "Context is null. Cannot route to next act")
    }




}