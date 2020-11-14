package com.example.michl.aimission.registerScene

interface IRegisterPresenter {
    fun onUserRegistrationSucceed(
            email: String,
            uuid: String
    )

    fun onUserRegistrationFailed(message: String)
}