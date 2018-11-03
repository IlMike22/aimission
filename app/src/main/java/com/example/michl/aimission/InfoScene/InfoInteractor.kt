package com.example.michl.aimission.InfoScene

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

interface InfoInteractorInput {
    fun loginUserWithUserCredentials(email: String, password: String)
    fun logoutUser()
}

class InfoInteractor : InfoInteractorInput {

    var output: InfoPresenterInput? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun logoutUser() {
        val uuid = firebaseAuth.currentUser?.uid
        firebaseAuth.signOut()
        output?.onUserLoggedOutSuccess(uuid?:"[noId]")
    }

    override fun loginUserWithUserCredentials(email: String, password: String) {
        Log.i("Aimission", "now login with firebase.")
        firebaseAuth = FirebaseAuth.getInstance()


        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
            var firebaseUser = firebaseAuth.currentUser
            if (firebaseUser?.uid != null)
                Log.i("Aimission", "User with uuid ${firebaseUser.uid} successfully logged in.")
            else {
                val errorMsg = "Unable to log in user. Cancelling."
                Log.e("Aimission", errorMsg)
                output?.onUserLoggedInError(errorMsg)
            }
        } catch (exception: Exception) {
            Log.e("Aimission", "Error during user auth firebase process. Details: ${exception.message}")
        }

        output?.onUserLoggedInSuccess(email, firebaseAuth.currentUser?.uid ?: "[noUserId]")
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
