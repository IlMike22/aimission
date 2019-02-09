package com.example.michl.aimission.MainScene

import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.MainScene.Views.MainFragmentInput
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.MonthItem
import java.lang.ref.WeakReference

interface MainPresenterInput
{
    fun onNoUserIdExists()
    fun onItemsLoadedSuccessfully(items:ArrayList<AimItem>)
    fun onItemsLoadedFailed(msg:String)
    fun onMonthItemsLoadedSuccessfully(monthItems:ArrayList<MonthItem>)
    fun onMonthItemsLoadedFailed(errorMsg:String)
}

class MainPresenter : MainPresenterInput
{
    //todo maybe the aim item functions are no longer needed here because in main fragment we always show month items only?

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

    override fun onMonthItemsLoadedSuccessfully(monthItems: ArrayList<MonthItem>) {
        output?.get()?.afterMonthItemsLoadedSuccessfully(monthItems)
    }

    override fun onMonthItemsLoadedFailed(errorMsg: String) {
        output?.get()?.afterMonthItemsLoadedFailed(errorMsg)
    }


}