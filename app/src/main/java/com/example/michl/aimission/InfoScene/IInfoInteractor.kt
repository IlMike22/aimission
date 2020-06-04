package com.example.michl.aimission.InfoScene

interface IInfoInteractor {
    fun loginUserWithUserCredentials(email: String, password: String)
    fun logoutUser()
    fun isUserLoggedIn()

}