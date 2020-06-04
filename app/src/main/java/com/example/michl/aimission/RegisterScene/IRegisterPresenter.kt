package com.example.michl.aimission.RegisterScene

interface IRegisterPresenter {
    fun onUserRegistrationSucceed(email: String, uuid: String)
    fun onUserRegistrationFailed(msg: String)
}