package com.example.michl.aimission.InfoScene

interface IInfoPresenter {
    fun onUserLoggedInError(email: String)
    fun onUserLoggedInSuccess(email: String, uid: String)
    fun onUserLoggedOutSuccess(uuid: String)
    fun onUserStatus(isLoggedIn: Boolean, uuid: String, email: String)

}