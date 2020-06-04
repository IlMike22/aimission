package com.example.michl.aimission.RegisterScene

interface IRegisterFragment {
    fun onRegisterClicked()
    fun validateInput(email: String, pswrd: String, pswrdRepeat: String): Boolean
    fun afterRegistrationSucceed(message: String)
    fun afterRegistrationFailed(errorMessage: String)
}