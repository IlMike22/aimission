package com.example.michl.aimission.RegisterScene

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

interface RegisterInteractorInput {
    fun registerUser(email: String, pswrd: String)
}

class RegisterInteractor : RegisterInteractorInput {
    val TAG = "Aimission"
    var output: RegisterPresenterInput? = null
    private lateinit var mAuth: FirebaseAuth

    override fun registerUser(email: String, pswrd: String) {
        Log.i(TAG, "Try to register user $email with pswrd $pswrd")
        try {
            mAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(email, pswrd).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i(TAG, "User registration was successful")
                    val user = mAuth.currentUser

                    // todo now create database entry with user id on table Person.


                    Log.i(TAG, "New users uuid is ${user?.uid}")

                    user?.apply {
                        output?.onUserRegistrationSucceed(email, uid)
                    }


                } else {
                    Log.e(TAG, "User registration was not successful. Details: ${it.exception}")
                    output?.onUserRegistrationFailed()
                }
            }
        } catch (exc: Exception) {
            Log.e(TAG, "Something went wrong during user registration. Details: ${exc.message}")
        }
    }
}