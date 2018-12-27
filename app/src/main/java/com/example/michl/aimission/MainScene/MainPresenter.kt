package com.example.michl.aimission.MainScene

import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.MainScene.Views.MainFragmentInput
import java.lang.ref.WeakReference

interface MainPresenterInput
{

}

class MainPresenter : MainPresenterInput
{
    var output : WeakReference<MainFragmentInput>? = null
}