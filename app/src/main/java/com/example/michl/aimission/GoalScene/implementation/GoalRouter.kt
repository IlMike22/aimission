package com.example.michl.aimission.GoalScene.implementation

import com.example.michl.aimission.GoalScene.views.GoalFragment
import java.lang.ref.WeakReference

interface GoalRouterInput {

}


class GoalRouter {
    var fragment: WeakReference<GoalFragment>? = null
}