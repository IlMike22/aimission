package com.example.michl.aimission.InfoScene.Views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.InfoScene.InfoConfigurator
import com.example.michl.aimission.InfoScene.InfoInteractorInput
import com.example.michl.aimission.InfoScene.InfoRouter
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.fragment_info.*

interface InfoFragmentInput {
    fun onLoginUserClicked(email: String, pswrd: String)
    fun onLogoutClicked()
    fun afterUserLoggedInError(errorMessage: String)
    fun afterUserLoggedInSuccess(successMessage: String)
    fun afterUserLoggedOutSuccess(message: String)

}

class InfoFragment : Fragment(), InfoFragmentInput {

    lateinit var output: InfoInteractorInput
    lateinit var router: InfoRouter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        InfoConfigurator.configure(this)

        frg_main_btnLogin.setOnClickListener {

            var email = frg_main_txt_email.text.toString()
            val pswrd = frg_main_txt_pswrd.text.toString()

            onLoginUserClicked(email, pswrd)
        }

        frg_main_btnLogout.setOnClickListener {
            onLogoutClicked()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onLoginUserClicked(email: String, pswrd: String) {
        // start login process
        Log.i("Aimission", "Trying to log in with username = $email and $pswrd")

        output?.loginUserWithUserCredentials(email, pswrd)
    }

    override fun onLogoutClicked() {
        output?.logoutUser()
    }

    override fun afterUserLoggedInError(errorMessage: String) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT)
    }

    override fun afterUserLoggedInSuccess(successMessage: String) {
        Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT)

        // disable login layout group, show logout layout group instead
        loginGroup.visibility = View.GONE
        logoutGroup.visibility = View.VISIBLE
    }

    override fun afterUserLoggedOutSuccess(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT)
    }
}
