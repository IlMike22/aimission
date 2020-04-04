package com.example.michl.aimission.GoalDetailScene

import com.example.michl.aimission.GoalDetailScene.Views.GoalDetailFragment
import java.lang.ref.WeakReference

interface GoalRouterInput {

}


class GoalRouter {
    var fragment: WeakReference<GoalDetailFragment>? = null
}