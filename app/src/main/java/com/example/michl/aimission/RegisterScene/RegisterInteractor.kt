package com.example.michl.aimission.RegisterScene

import android.util.Log
import com.example.michl.aimission.Utility.DbHelper
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
                    Log.i(TAG, "New users uuid is ${user?.uid}")

                    // crate new user id reference on aim table.
                    user?.let { user ->
                        DbHelper.createNewPersonReference(user.uid)
                        Log.i(TAG, "Created new user id dataset on aim table if has not already exists.")
                    }

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