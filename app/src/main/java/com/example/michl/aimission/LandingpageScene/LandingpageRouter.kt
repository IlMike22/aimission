package com.example.michl.aimission.LandingpageScene

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import com.example.michl.aimission.GoalsScene.Views.GoalsActivity
import com.example.michl.aimission.LandingpageScene.Views.LandingpageFragment
import java.lang.ref.WeakReference

class LandingpageRouter : ILandingpageRouter {
    var fragment: WeakReference<LandingpageFragment>? = null

    override fun openAimListView(
            context:Context,
            month: Int,
            year: Int
    ) {
                val intent = Intent(context, GoalsActivity::class.java)
                intent.putExtra("month", month)
                intent.putExtra("year", year)
                startActivity(context,intent,null)
    }

}