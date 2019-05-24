package com.example.michl.aimission.Utility

import android.util.Log
import com.example.michl.aimission.Models.AimItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DbHelper {


    companion object {

        const val TAG = "Aimission"

        /*
        Writes a new item in firebase database (table Aim)
        */
        fun saveNewAim(userId: String, item: AimItem): Boolean {
            return try {
                getAimTableReference().child(userId).child(item.id ?: "").setValue(item)
                true
            } catch (exception: Exception) {
                Log.e("Aimission", "Failed writing new item in table Aim. Details: ${exception.message}")
                false
            }
        }

        /*
        Deletes a specific aim item from table Aim
         */
        fun deleteAimItem(userId: String, itemId: String): Boolean {
            return try {
                getAimTableReference().child(userId).child(itemId).setValue(null)
                Log.i("Aimission", "Item was successfully deleted.")
                true
            } catch (exception: java.lang.Exception) {
                Log.e("Aimission", "An error occured while deleting item. Details: ${exception.message}")
                false
            }
        }

        // Creates a new person id reference on aim table if not exists.
        fun createNewPersonReference(userId: String): Boolean {
            return try {
                getAimTableReference().child(userId).setValue(null)
                true
            } catch (exc: java.lang.Exception) {
                Log.e(TAG, "An error occured while trying to add new user id dataset on aim table. Details: ${exc.message}")
                false
            }
        }


        private fun getAimTableReference(): DatabaseReference {
            val firebaseDb = FirebaseDatabase.getInstance()
            return firebaseDb.getReference("Aim")
        }
    }


}