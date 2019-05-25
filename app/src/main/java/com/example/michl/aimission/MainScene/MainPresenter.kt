package com.example.michl.aimission.MainScene

import com.example.michl.aimission.MainScene.Views.MainFragmentInput
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.MonthItem
import java.lang.ref.WeakReference

interface MainPresenterInput {
    fun onNoUserIdExists()
    fun onMonthItemsLoadedSuccessfully(aimItems:ArrayList<AimItem?>,monthItems: ArrayList<MonthItem>)
    fun onMonthItemsLoadedFailed(errorMsg: String)
    fun onEmptyMonthListLoaded(firstItem:MonthItem)
}

class MainPresenter : MainPresenterInput {

    var output: WeakReference<MainFragmentInput>? = null

    override fun onNoUserIdExists() {
        val errorMsg = "Couldn't find user id. Are you logged in?"
        output?.get()?.afterUserIdNotFound(errorMsg)
    }

    override fun onMonthItemsLoadedSuccessfully(aimItems:ArrayList<AimItem?>, monthItems: ArrayList<MonthItem>) {
        output?.get()?.afterMonthItemsLoadedSuccessfully(monthItems)
    }

    override fun onMonthItemsLoadedFailed(errorMsg: String) {
        output?.get()?.afterMonthItemsLoadedFailed(errorMsg)
    }

    override fun onEmptyMonthListLoaded(firstItem:MonthItem) {
        val msg = "At the moment there are no aims defined by you. Create now your first aim."
        output?.get()?.afterEmptyMonthListLoaded(msg, firstItem)
    }


}