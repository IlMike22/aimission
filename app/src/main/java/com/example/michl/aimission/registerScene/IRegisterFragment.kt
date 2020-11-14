package com.example.michl.aimission.registerScene

interface IRegisterFragment {
    fun onRegisterClicked()

    fun validateInput(
            email: String,
            password: String,
            repeatPassword: String
    ): Boolean

    fun afterRegistrationSucceed(message: String)
    fun afterRegistrationFailed(errorMessage: String)
}