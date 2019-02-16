package com.example.michl.aimission.AimListScene

import com.example.michl.aimission.AimListScene.Views.AimListFragmentInput
import com.example.michl.aimission.Models.AimItem
import java.lang.ref.WeakReference

interface AimListPresenterInput {
    fun onItemsLoadedSuccessfully(items: ArrayList<AimItem>)
    fun onNoUserIdExists()
}

class AimListPresenter : AimListPresenterInput {

    var output: WeakReference<AimListFragmentInput>? = null

    override fun onNoUserIdExists() {
        val msgUserNotFound = "Cannot authenticate current user. Are you already logged in?"
        output?.get()?.afterUserIdNotFound(msgUserNotFound)
    }

    override fun onItemsLoadedSuccessfully(items: ArrayList<AimItem>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}