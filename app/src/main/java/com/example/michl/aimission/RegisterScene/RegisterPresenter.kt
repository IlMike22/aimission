package com.example.michl.aimission.RegisterScene

import com.example.michl.aimission.RegisterScene.Views.RegisterFragmentInput
import java.lang.ref.WeakReference

interface RegisterPresenterInput {
    fun onUserRegistrationSucceed(email:String, uuid:String)
    fun onUserRegistrationFailed(msg:String)
}

class RegisterPresenter : RegisterPresenterInput {
    var output: WeakReference<RegisterFragmentInput>? = null

    override fun onUserRegistrationSucceed(email:String, uuid:String) {
       val message = "User $email was successfully created. The new uuid is $uuid"
       output?.get()?.afterRegistrationSucceed(message)
    }

    override fun onUserRegistrationFailed(msg:String) {
        val errorMessage = "Couldnt register user. $msg"
        output?.get()?.afterRegistrationFailed(errorMessage)
    }


}