package com.example.michl.aimission.AimDetailScene

import com.example.michl.aimission.AimDetailScene.Views.AimDetailFragment
import java.lang.ref.WeakReference

interface AimDetailRouterInput {

}


class AimDetailRouter {
    var fragment: WeakReference<AimDetailFragment>? = null
    val TAG = "AimDetailRouter"
}