package com.example.michl.aimission.RegisterScene.implementation

import com.example.michl.aimission.RegisterScene.IRegisterFragment
import com.example.michl.aimission.RegisterScene.IRegisterPresenter
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