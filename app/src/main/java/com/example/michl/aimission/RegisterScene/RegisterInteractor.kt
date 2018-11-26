package com.example.michl.aimission.RegisterScene

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

interface RegisterInteractorInput {
    fun registerUser(email: String, pswrd: String): Boolean
}

class RegisterInteractor : RegisterInteractorInput {
    val TAG = "Aimission"
    var output: RegisterPresenterInput? = null
    private lateinit var mAuth: FirebaseAuth

    override fun registerUser(email: String, pswrd: String): Boolean {
        Log.i(TAG, "Try to register user $email with pswrd $pswrd")
        try {
            mAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(email, pswrd).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i(TAG, "User registration was successful")
                    val user = mAuth.currentUser
                    Log.i(TAG, "New users uuid is ${user?.uid}")

                    // todo update ui with user data

                } else {
                    Log.e(TAG, "User registration was not successful")
                }
            }
        } catch (exc: Exception) {
            Log.e(TAG, "Something went wrong during user registration. Details: ${exc.message}")
        }



        return false
    }

}