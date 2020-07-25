package com.example.michl.aimission.InfoScene.implementation

import android.util.Log
import com.example.michl.aimission.InfoScene.IInfoInteractor
import com.example.michl.aimission.InfoScene.IInfoPresenter
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth

class InfoInteractor : IInfoInteractor {
    var output: IInfoPresenter? = null
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun logoutUser() {
        val uuid = firebaseAuth.currentUser?.uid
        firebaseAuth.signOut()
        Log.i(TAG, "User successfully logged out.")
        output?.onUserLoggedOutSuccess(uuid ?: "[noId]")
    }

    override fun loginUserWithUserCredentials(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                var firebaseUser = firebaseAuth.currentUser
                if (firebaseUser?.uid != null) {
                    Log.i(TAG, "User with uuid ${firebaseUser.uid} successfully logged in.")
                    output?.onUserLoggedInSuccess(email, firebaseAuth.currentUser?.uid ?: "[noUserId]")
                } else {
                    val errorMsg = "Unable to log in user $email."
                    Log.e(TAG, errorMsg)
                    output?.onUserLoggedInError(email)
                }
            }

        } catch (exception: Exception) {
            Log.e(TAG, "Error during user auth firebase process. Details: ${exception.message}")
        }
    }

    override fun isUserLoggedIn() {
        var currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            Log.i(TAG, "User is logged in. ID is ${currentUser.uid}")
            output?.onUserStatus(true,currentUser.uid, currentUser.email.toString())
        } else {
            Log.i(TAG, "User is not loggged in.")
            output?.onUserStatus(false,"","")
        }
    }
}