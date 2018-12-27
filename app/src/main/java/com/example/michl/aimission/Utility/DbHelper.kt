package com.example.michl.aimission.Utility

import android.content.ClipData
import android.util.Log
import com.example.michl.aimission.Models.AimItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DbHelper {


    companion object {

        val TAG = "DBHelper"

        /*
        Writes a new item in firebase database (table Aim)
        */
        fun saveNewAim(userId: String, item: AimItem): Boolean {
            try {
                getAimTableReference().child(userId).child(item.id).setValue(item)
                return true
            } catch (exception: Exception) {
                Log.e("Aimission", "Failed writing new item in table Aim. Details: ${exception.message}")
                return false
            }
        }
//
//        fun getAllAimsForUser(userId:String):ArrayList<AimItem>
//        {
//            val aims = ArrayList<AimItem>()
//
//            getAimTableReference().
//
//        }

        /*
        Deletes a specific aim item from table Aim
         */
        fun deleteAimItem(userId: String, itemId: String): Boolean {
            try {
                getAimTableReference().child(userId).child(itemId).setValue(null)
                Log.i("Aimission", "Item was successfully deleted.")
                return true
            } catch (exception: java.lang.Exception) {
                Log.e("Aimission", "An error occured while deleting item. Details: ${exception.message}")
                return false
            }
        }

        // Creates a new person id reference on aim table if not exists.
        // todo maybe we dont need this.
        fun createNewPersonReference(userId: String): Boolean {
            try {
                getAimTableReference().child(userId).setValue(null)
                return true
            } catch (exc: java.lang.Exception) {
                Log.e(TAG, "An error occured while trying to add new user id dataset on aim table. Details: ${exc.message}")
                return false
            }
        }


        private fun getAimTableReference(): DatabaseReference {
            val firebaseDb = FirebaseDatabase.getInstance()
            return firebaseDb.getReference("Aim")
        }

        private fun getPersonTableReference(): DatabaseReference
        {
            val firebaseDB = FirebaseDatabase.getInstance()
            return firebaseDB.getReference("Person")
        }
    }


}