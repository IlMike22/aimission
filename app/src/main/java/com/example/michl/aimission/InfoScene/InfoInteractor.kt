package com.example.michl.aimission.InfoScene

import android.util.Log
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth

interface InfoInteractorInput {
    fun loginUserWithUserCredentials(email: String, password: String)
    fun logoutUser()
    fun isUserLoggedIn()
}

class InfoInteractor : InfoInteractorInput {

    var output: InfoPresenterInput? = null
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


//        // sample auth firebase with user credentials
//        firebaseAuth.signInWithEmailAndPassword(email,password)
//                .addOnCompleteListener(activity as Activity) { task ->
//                    if(task.isSuccessful)
//                    {
//
//                        // todo delete later -> sample data for writing and deleting with dbHelper class
//
//                        Log.i("Aimission","Auth process success")
//                        var aimItem = AimItem("422","My new updated aim item",3,"This is my first aim item saved in firebase",1,2,0)
//                        var anotherAimItem = AimItem("343","A scnd item",1,"Anche questa é disponibile",3,1,0)
//                        var userId = task.result?.user?.uid
//                        Log.i("Aimission","User ID is $userId")
//
//                        userId?.apply {
//                            DbHelper.saveNewAim(this, aimItem)
//                            DbHelper.saveNewAim(this, anotherAimItem)
//                            DbHelper.deleteAimItem(this,anotherAimItem.id)
//                        }
//
//                    }
//                    else
//                    {
//                        Log.i("Aimission","no success")
//                    }
//                }
