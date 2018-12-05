package com.example.michl.aimission.RegisterScene

import com.example.michl.aimission.RegisterScene.Views.RegisterFragmentInput
import java.lang.ref.WeakReference

interface RegisterPresenterInput {
    fun onUserRegistrationSucceed(email:String, uuid:String)
    fun onUserRegistrationFailed()
}

class RegisterPresenter : RegisterPresenterInput {
    var output: WeakReference<RegisterFragmentInput>? = null

    override fun onUserRegistrationSucceed(email:String, uuid:String) {
       val message = "User $email was successfully created. The new uuid is $uuid"
       output?.get()?.afterRegistrationSucceed(message)
    }

    override fun onUserRegistrationFailed() {
        val errorMessage = "Couldnt register user. Please try again"
        output?.get()?.afterRegistrationFailed(errorMessage)
    }


}