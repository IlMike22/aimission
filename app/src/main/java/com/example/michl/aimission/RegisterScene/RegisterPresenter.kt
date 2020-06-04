package com.example.michl.aimission.RegisterScene

import java.lang.ref.WeakReference

class RegisterPresenter : IRegisterPresenter {
    var output: WeakReference<IRegisterFragment>? = null

    override fun onUserRegistrationSucceed(email:String, uuid:String) {
       val message = "User $email was successfully created. The new uuid is $uuid"
       output?.get()?.afterRegistrationSucceed(message)
    }

    override fun onUserRegistrationFailed(msg:String) {
        val errorMessage = "Couldnt register user. $msg"
        output?.get()?.afterRegistrationFailed(errorMessage)
    }
}