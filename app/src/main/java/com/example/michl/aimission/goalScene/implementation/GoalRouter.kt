package com.example.michl.aimission.goalScene.implementation

import com.example.michl.aimission.goalScene.views.GoalFragment
import java.lang.ref.WeakReference

interface GoalRouterInput {

}


class GoalRouter {
    var fragment: WeakReference<GoalFragment>? = null
}