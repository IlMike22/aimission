package com.example.michl.aimission.LandingpageScene.implementation

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.michl.aimission.GoalsScene.views.GoalsActivity
import com.example.michl.aimission.LandingpageScene.ILandingpageRouter
import com.example.michl.aimission.LandingpageScene.views.LandingpageFragment
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