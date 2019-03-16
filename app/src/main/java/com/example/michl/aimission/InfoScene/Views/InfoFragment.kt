package com.example.michl.aimission.InfoScene.Views

import android.content.Intent
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
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import kotlinx.android.synthetic.main.fragment_info.*

interface InfoFragmentInput {
    fun onLoginUserClicked(email: String, pswrd: String)
    fun onLogoutClicked()
    fun afterUserLoggedInError(errorMessage: String)
    fun afterUserLoggedInSuccess(email: String, uuid: String, successMessage: String)
    fun afterUserLoggedOutSuccess(message: String)
    fun afterCheckedIfUserIsLoggedIn(isLoggedIn: Boolean, uuid: String, email: String)
    fun onRegisterClicked()
}

const val REQUEST_USER_REGISTER_SUCCEED = 1

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

        frg_main_btnRegister.setOnClickListener {
            onRegisterClicked()
        }

        output?.isUserLoggedIn()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_USER_REGISTER_SUCCEED)
        {
            Log.i(DbHelper.TAG, "We have a result. User registered correctly. Now loading user profile")
            // todo implement function for loading current user and showing ui change
        }
        else
            Log.i(TAG, "no result givven.")
    }

    override fun onLoginUserClicked(email: String, pswrd: String) {

        if (email.isEmpty() || pswrd.isEmpty()) {
            Toast.makeText(activity, "You must enter email and password to log in.", Toast.LENGTH_SHORT).show()
            return
        }
        // start login process
        Log.i("Aimission", "Trying to log in with username = $email and $pswrd")
        showLoadingStatus()
        output?.loginUserWithUserCredentials(email, pswrd)
    }

    override fun onLogoutClicked() {
        output?.logoutUser()
    }

    override fun afterUserLoggedInError(errorMessage: String) {
        progressBar.visibility = View.GONE
        showLoginStatus()
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun afterUserLoggedInSuccess(email: String, uuid: String, successMessage: String) {

        progressBar.visibility = View.GONE
        Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show()
        // disable login layout group, show logout layout group instead
        showLogoutStatus()
        showUserInformation(email, uuid)

    }

    override fun afterUserLoggedOutSuccess(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        showLoginStatus()
    }

    override fun afterCheckedIfUserIsLoggedIn(isLoggedIn: Boolean, uuid: String, email: String) {
        if (isLoggedIn) {
            showLogoutStatus()
            showUserInformation(email, uuid)
            Log.i(TAG, "User is logged in. UUID is $uuid, email is $email")
        } else {
            showLoginStatus()
            Log.i(TAG, "User is not logged in.")
        }
    }

    override fun onRegisterClicked() {
        activity?.apply {
            router?.openRegisterView(this)

        }
    }

    private fun showLoginStatus() {
        logoutGroup.visibility = View.GONE
        loginGroup.visibility = View.VISIBLE
    }

    private fun showLogoutStatus() {
        loginGroup.visibility = View.GONE
        logoutGroup.visibility = View.VISIBLE
    }

    private fun showLoadingStatus() {
        progressBar.visibility = View.VISIBLE
        loginGroup.visibility = View.GONE
        loginGroup.visibility = View.GONE
    }

    private fun showUserInformation(email: String, uuid: String) {
        frg_main_tvUserInfo.text = "Hello $email. You are successfully logged in. Your uuid is $uuid"
    }
}
