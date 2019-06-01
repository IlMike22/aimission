package com.example.michl.aimission.Utility

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.michl.aimission.Models.AimItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DbHelper {


    companion object {

        const val TAG = "Aimission"


        /*
        Writes or updates a new item in table Aim.
        */
        fun createOrUpdateAimItem(userId: String, item: AimItem): Boolean {
            return try {
                getAimTableReference().child(userId).child(item.id ?: "").setValue(item)
                true
            } catch (exception: Exception) {
                Log.e("Aimission", "Failed writing new item in table Aim. Details: ${exception.message}")
                false
            }
        }

        /*
        Deletes a specific aim item from table Aim.
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


        fun getAimTableReference(): DatabaseReference {
            val firebaseDb = FirebaseDatabase.getInstance()
            return firebaseDb.getReference("Aim")
        }

        fun getCurrentUserId(): String {
            return FirebaseAuth.getInstance().currentUser?.uid ?: ""
        }

        fun getSharedPrefsInstance(context: Context): SharedPreferences {
            return context.getSharedPreferences("com.example.michl.aimission", Context.MODE_PRIVATE)
        }

        @SuppressLint("CommitPrefEdits")
        fun storeInSharedPrefs(context: Context, key: String, value: Any): Boolean {
            //todo implement this
            val sharedPrefs = getSharedPrefsInstance(context)
            when (value) {
                is Boolean -> {
                    sharedPrefs.edit().putBoolean(key, value)
                    return true
                }

                is String -> {
                    sharedPrefs.edit().putString(key, value)
                    return true
                }

                is Int -> {
                    sharedPrefs.edit().putInt(key, value)
                    return true
                }
                else -> {
                    Log.e(TAG, "Unknown data type. Cannot save it into shared prefs.")
                    return false
                }
            }
        }

        fun getSharedPrefsValueAsInt(context: Context, key: String): Int {
            val sharedPrefs = getSharedPrefsInstance(context)
            return sharedPrefs.getInt(key, -1)
        }

        fun getSharedPrefsValueAsString(context: Context, key: String): String {
            val sharedPrefs = getSharedPrefsInstance(context)
            return sharedPrefs.getString(key, "")
        }

        fun getSharedPrefsValueAsBoolean(context: Context, key: String): Boolean {
            val sharedPrefs = getSharedPrefsInstance(context)
            return sharedPrefs.getBoolean(key, false)
        }
    }


}