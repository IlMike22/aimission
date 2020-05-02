package com.example.michl.aimission.GoalDetailScene

import com.example.michl.aimission.GoalDetailScene.Views.GoalFragment
import java.lang.ref.WeakReference

interface GoalRouterInput {

}


class GoalRouter {
    var fragment: WeakReference<GoalFragment>? = null
}