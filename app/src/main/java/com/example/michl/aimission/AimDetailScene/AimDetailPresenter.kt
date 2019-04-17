package com.example.michl.aimission.AimDetailScene

import com.example.michl.aimission.AimDetailScene.Views.AimDetailFragmentInput
import com.example.michl.aimission.Models.AimItem
import java.lang.ref.WeakReference

interface AimDetailPresenterInput
{
    fun validateFirebaseUser(userID:String)
    fun onAimStoredSuccessfully()
    fun onAimStoredFailed()
    fun onAimReadSuccessfully(item:AimItem)
}

class AimDetailPresenter: AimDetailPresenterInput
{


    var output:WeakReference<AimDetailFragmentInput>? = null

    override fun validateFirebaseUser(userID: String) {
        if (userID.isNullOrEmpty())
            output?.get()?.onFirebaseUserNotExists("Firebase ID not found. Are you logged in? Otherwise there cannot be created a new aim")
        else
            output?.get()?.onFirebaseUserExists(userID)
    }

    override fun onAimStoredSuccessfully() {
        output?.get()?.afterAimStoredSuccessfully()
    }

    override fun onAimStoredFailed() {
        output?.get()?.afterAimStoredFailed()
    }
    override fun onAimReadSuccessfully(item:AimItem) {
        output?.get()?.showAimDetailData(item)
    }


}

