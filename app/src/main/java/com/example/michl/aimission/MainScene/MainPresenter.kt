package com.example.michl.aimission.MainScene

import com.example.michl.aimission.MainScene.Views.MainFragment
import com.example.michl.aimission.MainScene.Views.MainFragmentInput
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.MonthItem
import java.lang.ref.WeakReference

interface MainPresenterInput
{
    fun onNoUserIdExists()
    fun onMonthItemsLoadedSuccessfully(monthItems:ArrayList<MonthItem>)
    fun onMonthItemsLoadedFailed(errorMsg:String)
}

class MainPresenter : MainPresenterInput
{
    var output : WeakReference<MainFragmentInput>? = null

    override fun onNoUserIdExists() {
        val errorMsg = "Couldn't find user id. Are you logged in?"
        output?.get()?.afterUserIdNotFound(errorMsg)
    }

    override fun onMonthItemsLoadedSuccessfully(monthItems: ArrayList<MonthItem>) {
        output?.get()?.afterMonthItemsLoadedSuccessfully(monthItems)
    }

    override fun onMonthItemsLoadedFailed(errorMsg: String) {
        output?.get()?.afterMonthItemsLoadedFailed(errorMsg)
    }


}