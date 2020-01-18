package com.example.michl.aimission.MainScene

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import com.example.michl.aimission.AimListScene.Views.AimListActivity
import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.Utility.Aimission
import java.lang.ref.WeakReference

interface MainRouterInput {
    fun openAimListView(context:Context,month: Int, year: Int)
}

class MainRouter : MainRouterInput {

    var fragment: WeakReference<MainFragment>? = null

    override fun openAimListView(
            context:Context,
            month: Int,
            year: Int
    ) {
                val intent = Intent(context, AimListActivity::class.java)
                intent.putExtra("month", month)
                intent.putExtra("year", year)
                startActivity(context,intent,null)
    }

}