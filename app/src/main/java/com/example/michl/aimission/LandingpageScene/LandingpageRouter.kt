package com.example.michl.aimission.LandingpageScene

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import com.example.michl.aimission.GoalsScene.Views.GoalsActivity
import com.example.michl.aimission.LandingpageScene.Views.LandingpageFragment
import com.example.michl.aimission.Models.Month
import java.lang.ref.WeakReference

class LandingpageRouter : ILandingpageRouter {
    var fragment: WeakReference<LandingpageFragment>? = null

    override fun openGoals(
            context:Context,
            month: Month
    ) {
                val intent = Intent(context, GoalsActivity::class.java)
                intent.putExtra("month", month)
                startActivity(context,intent,null)
    }

}