package com.example.michl.aimission.AimDetailScene

import com.example.michl.aimission.AimDetailScene.Views.AimDetailFragment
import java.lang.ref.WeakReference

interface GoalRouterInput {

}


class GoalRouter {
    var fragment: WeakReference<AimDetailFragment>? = null
}