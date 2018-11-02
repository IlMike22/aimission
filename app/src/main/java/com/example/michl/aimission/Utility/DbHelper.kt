package com.example.michl.aimission.Utility

import android.util.Log
import com.example.michl.aimission.Models.AimItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DbHelper {

    companion object {
        /*
        Writes a new item in firebase database (table Aim)
        */
        fun saveNewAim(userId:String, item: AimItem): Boolean {
            try {
                getAimTableReference().child(userId).child(item.id).setValue(item)
                return true
            } catch (exception: Exception) {
                Log.e("Aimission", "Failed writing new item in table Aim. Details: ${exception.message}")
                return false
            }
        }

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



        private fun getAimTableReference(): DatabaseReference {
            var firebaseDb = FirebaseDatabase.getInstance()
            return firebaseDb.getReference("Aim")
        }
    }


}