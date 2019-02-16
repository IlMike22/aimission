package com.example.michl.aimission.AimListScene

import com.example.michl.aimission.AimListScene.Views.AimListFragment
import java.lang.ref.WeakReference


interface AimListRouterInput {

}

class AimListRouter : AimListRouterInput {
    var fragment: WeakReference<AimListFragment>? = null
}