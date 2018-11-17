package com.example.michl.aimission.InfoScene

import com.example.michl.aimission.InfoScene.Views.InfoFragmentInput
import java.lang.ref.WeakReference

interface InfoPresenterInput {
    fun onUserLoggedInError(email: String)
    fun onUserLoggedInSuccess(email: String, uid: String)
    fun onUserLoggedOutSuccess(uuid: String)
    fun onUserStatus(isLoggedIn:Boolean, uuid:String, email:String)
}

class InfoPresenter : InfoPresenterInput {

    var output: WeakReference<InfoFragmentInput>? = null

    override fun onUserLoggedInError(email: String) {
        val errorMessage = "Unable to log in user with email $email. Are you sure the user exists?"
        output?.get()?.afterUserLoggedInError(errorMessage)
    }

    override fun onUserLoggedInSuccess(email: String, uid: String) {
        val successMessage = "User $email successfully logged in. The uid is $uid"
        output?.get()?.afterUserLoggedInSuccess(email, uid, successMessage)
    }

    override fun onUserLoggedOutSuccess(uuid: String) {
        val successMessage = "User $uuid was successfully logged out."
        output?.get()?.afterUserLoggedOutSuccess(successMessage)
    }

    override fun onUserStatus(isLoggedIn: Boolean, uuid:String, email:String) {
        output?.get()?.afterCheckedIfUserIsLoggedIn(isLoggedIn, uuid, email)
    }

}