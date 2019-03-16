package com.example.michl.aimission.RegisterScene.Views

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.InfoScene.REQUEST_USER_REGISTER_SUCCEED
import com.example.michl.aimission.R
import com.example.michl.aimission.RegisterScene.RegisterConfigurator
import com.example.michl.aimission.RegisterScene.RegisterInteractorInput
import com.example.michl.aimission.RegisterScene.RegisterRouter
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import kotlinx.android.synthetic.main.fragment_register.*

interface RegisterFragmentInput {
    fun onRegisterClicked()
    fun validateInput(email: String, pswrd: String, pswrdRepeat: String): Boolean
    fun afterRegistrationSucceed(message: String)
    fun afterRegistrationFailed(errorMessage: String)
}


class RegisterFragment : Fragment(), RegisterFragmentInput {

    lateinit var router: RegisterRouter
    lateinit var output: RegisterInteractorInput


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // init configurator
        RegisterConfigurator.configure(this)

        btnRegister.setOnClickListener {

            onRegisterClicked()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRegisterClicked() {
        Log.i(TAG, "Starting register process")
        val email = txtEmail.text.toString()
        val pswrd = txtPswrd.text.toString()
        val pswrdRepeat = txtPswrdRepeat.text.toString()

        if (validateInput(email, pswrd, pswrdRepeat)) {
            output.registerUser(email, pswrd)
        }
    }

    override fun validateInput(email: String, pswrd: String, pswrdRepeat: String): Boolean {

        if (email.isEmpty() || pswrd.isEmpty() || pswrdRepeat.isEmpty()) {
            Toast.makeText(context, "Please fill all the input fields to register.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.isValidEmail()) {
            Toast.makeText(context, "Please type in a valid email address.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!pswrd.isValidPswrdLength()) {
            Toast.makeText(context, "Please define a password with at least 6 characters.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!pswrd.equals(pswrdRepeat)) {
            Toast.makeText(context, "Your password repeat is not the same as your password.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun afterRegistrationSucceed(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        activity?.setResult(REQUEST_USER_REGISTER_SUCCEED)
        activity?.finish() // todo is it enough to only close activity? has to be refreshed. needs to be startactivityforresult
    }

    override fun afterRegistrationFailed(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    // extension for validating email adress
    private fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()

    // extension for validating password length
    private fun String.isValidPswrdLength(): Boolean = this.length >= 6
}
