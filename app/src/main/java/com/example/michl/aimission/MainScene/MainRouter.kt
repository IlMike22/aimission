package com.example.michl.aimission.MainScene

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import com.example.michl.aimission.GoalsScene.Views.AimListActivity
import com.example.michl.aimission.MainScene.Views.MainFragment
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