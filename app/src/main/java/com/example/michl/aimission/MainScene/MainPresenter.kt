package com.example.michl.aimission.MainScene

import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.MainScene.Views.MainFragmentInput
import java.lang.ref.WeakReference

interface MainPresenterInput
{
    fun onNoUserIdExists()
    fun onItemsLoadedSuccessfully()
    fun onItemsLoadedFailed(msg:String)
}

class MainPresenter : MainPresenterInput
{
    var output : WeakReference<MainFragmentInput>? = null

    override fun onNoUserIdExists() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemsLoadedSuccessfully() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemsLoadedFailed(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}