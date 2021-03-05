package com.example.michl.aimission.infoScene.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.michl.aimission.infoScene.IInfoFragment
import com.example.michl.aimission.infoScene.IInfoInteractor
import com.example.michl.aimission.infoScene.implementation.InfoConfigurator
import com.example.michl.aimission.infoScene.implementation.InfoRouter
import com.example.michl.aimission.R
import com.example.michl.aimission.utitlity.DbHelper
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import kotlinx.android.synthetic.main.fragment_info.*

const val REQUEST_USER_REGISTER_SUCCEED = 1

class InfoFragment : Fragment(), IInfoFragment {
    lateinit var output: IInfoInteractor
    lateinit var router: InfoRouter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InfoConfigurator.configure(this)

        button_info_login.setOnClickListener {

            val email = edit_text_info_email.text.toString()
            val password = edit_text_info_password.text.toString()

            onLoginUserClicked(email, password)
        }

        button_info_logout.setOnClickListener {
            onLogoutClicked()
        }

        button_info_register.setOnClickListener {
            onRegisterClicked()
        }

        output.isUserLoggedIn()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_USER_REGISTER_SUCCEED)
        {
            Log.i(DbHelper.TAG, "We have a result. User registered correctly. Now loading user profile")
            // todo implement function for loading current user and showing ui change
        }
        else
            Log.i(TAG, "no result given.")
    }

    override fun onLoginUserClicked(email: String, pswrd: String) {

        if (email.isEmpty() || pswrd.isEmpty()) {
            Toast.makeText(activity, "You must enter email and password to log in.", Toast.LENGTH_SHORT).show()
            return
        }
        // start login process
        Log.i("Aimission", "Trying to log in with username = $email and $pswrd")
        showLoadingStatus()
        output.loginUserWithUserCredentials(email, pswrd)
    }

    override fun onLogoutClicked() {
        output.logoutUser()
    }

    override fun afterUserLoggedInError(errorMessage: String) {
        progress_bar_info?.visibility = View.GONE
        showLoginStatus()
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun afterUserLoggedInSuccess(email: String, uuid: String, successMessage: String) {
        progress_bar_info.visibility = View.GONE
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
            router.openRegisterView(this)

        }
    }

    private fun showLoginStatus() {
        group_info_logout.visibility = View.GONE
        group_info_login.visibility = View.VISIBLE
    }

    private fun showLogoutStatus() {
        group_info_login.visibility = View.GONE
        group_info_logout.visibility = View.VISIBLE
    }

    private fun showLoadingStatus() {
        progress_bar_info.visibility = View.VISIBLE
        group_info_login.visibility = View.GONE
        group_info_login.visibility = View.GONE
    }

    private fun showUserInformation(email: String, uuid: String) {
        text_view_info_user_info.text = "Hello $email. You are successfully logged in. Your uuid is $uuid"
    }
}
