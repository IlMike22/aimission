package com.example.michl.aimission.RegisterScene

import com.example.michl.aimission.InfoScene.Views.InfoFragment
import com.example.michl.aimission.RegisterScene.Views.RegisterFragment
import java.lang.ref.WeakReference

interface RegisterRouterInput
{
    //todo implement later
}

class RegisterRouter:RegisterRouterInput
{
    var fragment: WeakReference<RegisterFragment>? = null
    val TAG = "Aimission"
}