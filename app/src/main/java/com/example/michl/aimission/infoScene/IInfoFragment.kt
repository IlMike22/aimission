package com.example.michl.aimission.infoScene

interface IInfoFragment {
    fun onLoginUserClicked(email: String, pswrd: String)

    fun onLogoutClicked()

    fun afterUserLoggedInError(errorMessage: String)

    fun afterUserLoggedInSuccess(
            email: String,
            uuid: String,
            successMessage: String
    )

    fun afterUserLoggedOutSuccess(message: String)

    fun afterCheckedIfUserIsLoggedIn(
            isLoggedIn: Boolean,
            uuid: String,
            email: String
    )

    fun onRegisterClicked()
}