package com.example.michl.aimission.registerScene.implementation

import com.example.michl.aimission.registerScene.IRegisterFragment
import com.example.michl.aimission.registerScene.IRegisterPresenter
import java.lang.ref.WeakReference

class RegisterPresenter : IRegisterPresenter {
    var output: WeakReference<IRegisterFragment>? = null

    override fun onUserRegistrationSucceed(email:String, uuid:String) {
       val message = "User $email was successfully created. The new uuid is $uuid"
       output?.get()?.afterRegistrationSucceed(message)
    }

    override fun onUserRegistrationFailed(message:String) {
        val errorMessage = "Couldnt register user. $message"
        output?.get()?.afterRegistrationFailed(errorMessage)
    }
}