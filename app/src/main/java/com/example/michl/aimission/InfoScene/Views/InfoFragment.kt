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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_info.*

interface InfoFragmentInput {
    fun onLoginUserClicked(email: String, pswrd: String)
    fun onLogoutClicked()
    fun afterUserLoggedInError(errorMessage: String)
    fun afterUserLoggedInSuccess(email: String, uuid: String, successMessage: String)
    fun afterUserLoggedOutSuccess(message: String)
    fun afterCheckedIfUserIsLoggedIn(isLoggedIn: Boolean, uuid: String, email: String)

}

class InfoFragment : Fragment(), InfoFragmentInput {

    lateinit var output: InfoInteractorInput
    lateinit var router: InfoRouter
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG = "Aimission"

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

        output?.isUserLoggedIn()

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
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun afterUserLoggedInSuccess(email: String, uuid: String, successMessage: String) {
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

    private fun showLoginStatus() {
        logoutGroup.visibility = View.GONE
        loginGroup.visibility = View.VISIBLE
    }

    private fun showLogoutStatus() {
        loginGroup.visibility = View.GONE
        logoutGroup.visibility = View.VISIBLE
    }

    private fun showUserInformation(email: String, uuid: String) {
        frg_main_tvUserInfo.text = "Hello $email. You are successfully logged in. Your uuid is $uuid"
    }
}
