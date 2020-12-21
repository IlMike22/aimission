package com.example.michl.aimission.infoScene

interface IInfoInteractor {
    fun loginUserWithUserCredentials(
            email: String,
            password: String
    )

    fun logoutUser()

    fun isUserLoggedIn()

}