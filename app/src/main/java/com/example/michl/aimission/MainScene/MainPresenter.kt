package com.example.michl.aimission.MainScene

import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.MainScene.Views.MainFragmentInput
import com.example.michl.aimission.Models.AimItem
import java.lang.ref.WeakReference

interface MainPresenterInput
{
    fun onNoUserIdExists()
    fun onItemsLoadedSuccessfully(items:ArrayList<AimItem>)
    fun onItemsLoadedFailed(msg:String)
}

class MainPresenter : MainPresenterInput
{
    var output : WeakReference<MainFragmentInput>? = null

    override fun onNoUserIdExists() {
        val errorMsg = "Couldn't find user id. Are you logged in?"
        output?.get()?.afterUserIdNotFound(errorMsg)
    }

    override fun onItemsLoadedSuccessfully(items:ArrayList<AimItem>) {
        //todo maybe we can format aim data here before print it out..
        output?.get()?.showAllUserItems(items)
    }

    override fun onItemsLoadedFailed(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}