package com.example.michl.aimission.registerScene.views

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.michl.aimission.infoScene.implementation.REQUEST_USER_REGISTER_SUCCEED
import com.example.michl.aimission.R
import com.example.michl.aimission.registerScene.IRegisterFragment
import com.example.michl.aimission.registerScene.IRegisterInteractor
import com.example.michl.aimission.registerScene.implementation.RegisterConfigurator
import com.example.michl.aimission.registerScene.implementation.RegisterRouter
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(), IRegisterFragment {
    lateinit var router: RegisterRouter
    lateinit var output: IRegisterInteractor

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        RegisterConfigurator.configure(this)

        button_register_register.setOnClickListener {
            onRegisterClicked()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRegisterClicked() {
        val email = edit_text_register_email.text.toString()
        val pswrd = edit_text_register_password.text.toString()
        val pswrdRepeat = edit_text_register_password_repeat.text.toString()

        if (validateInput(email, pswrd, pswrdRepeat)) {
            output.registerUser(email, pswrd)
        }
    }

    override fun validateInput(email: String, password: String, repeatPassword: String): Boolean {
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(context, getString(R.string.register_error_empty_fields), Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.isValidEmail()) {
            Toast.makeText(context, getString(R.string.register_error_wrong_email_address), Toast.LENGTH_SHORT).show()
            return false
        }

        if (!password.isValidPswrdLength()) {
            Toast.makeText(context, getString(R.string.register_error_password_too_short), Toast.LENGTH_SHORT).show()
            return false
        }

        if (!password.equals(repeatPassword)) {
            Toast.makeText(context, getString(R.string.register_error_wrong_password_repeat), Toast.LENGTH_SHORT).show()
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

    private fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun String.isValidPswrdLength(): Boolean = this.length >= 6
}
